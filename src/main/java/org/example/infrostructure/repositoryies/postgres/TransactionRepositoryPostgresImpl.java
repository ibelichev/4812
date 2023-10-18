package org.example.infrostructure.repositoryies.postgres;

import org.example.core.models.Transaction;
import org.example.core.models.enums.AuditableStatus;
import org.example.core.models.enums.TransactionType;
import org.example.core.repositories.TransactionRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;


/**
 * Реализация интерфейса {@link TransactionRepository},
 * предоставляющая функциональность для работы с транзакциями в системе,
 * через PostgreSql
 */
public class TransactionRepositoryPostgresImpl implements TransactionRepository {
    private final Connection connection;

    public TransactionRepositoryPostgresImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addTransaction(Transaction transaction) {
        String insertSql = "INSERT INTO entities.\"Transaction\" (user_id, date_time, type, status, amount) VALUES (?, ?, ?, ?, ?) RETURNING id";

        try (PreparedStatement statement = connection.prepareStatement(insertSql)) {
            statement.setLong(1, transaction.getUserId());
            statement.setObject(2, transaction.getDateTime());
            statement.setString(3, transaction.getType().toString());
            statement.setString(4, transaction.getStatus().toString());
            statement.setFloat(5, transaction.getAmount());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Устанавливаем сгенерированный базой данных идентификатор в объект транзакции
                long id = resultSet.getLong(1);
                transaction.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Transaction findById(long id) {
        String selectSql = "SELECT * FROM entities.\"Transaction\" WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(selectSql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                long userId = resultSet.getLong("user_id");
                LocalDateTime dateTime = resultSet.getObject("date_time", LocalDateTime.class);
                TransactionType type = TransactionType.valueOf(resultSet.getString("type"));
                AuditableStatus status = AuditableStatus.valueOf(resultSet.getString("status"));
                float amount = resultSet.getFloat("amount");
                return new Transaction(id, userId, dateTime, type, status, amount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Transaction> findAllByUserId(long userId) {
        List<Transaction> userTransactions = new ArrayList<>();
        String selectSql = "SELECT * FROM entities.\"Transaction\" WHERE user_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(selectSql)) {
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                long transactionId = resultSet.getLong("id");
                LocalDateTime dateTime = resultSet.getObject("date_time", LocalDateTime.class);
                TransactionType type = TransactionType.valueOf(resultSet.getString("type"));
                AuditableStatus status = AuditableStatus.valueOf(resultSet.getString("status"));
                float amount = resultSet.getFloat("amount");
                userTransactions.add(new Transaction(transactionId, userId, dateTime, type, status, amount));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userTransactions;
    }

    @Override
    public List<Transaction> getAll() {
        List<Transaction> allTransactions = new ArrayList<>();
        String selectSql = "SELECT * FROM entities.\"Transaction\"";

        try (PreparedStatement statement = connection.prepareStatement(selectSql)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                long transactionId = resultSet.getLong("id");
                long userId = resultSet.getLong("user_id");
                LocalDateTime dateTime = resultSet.getObject("date_time", LocalDateTime.class);
                TransactionType type = TransactionType.valueOf(resultSet.getString("type"));
                AuditableStatus status = AuditableStatus.valueOf(resultSet.getString("status"));
                float amount = resultSet.getFloat("amount");
                allTransactions.add(new Transaction(transactionId, userId, dateTime, type, status, amount));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allTransactions;
    }

    /**
     * Не используется в реализации логики репозиториев через Postgres,
     * т.к. id генерируются через сиквенсы
     */
    @Override
    public boolean isTransactionIdUnique(long transactionId) {
        Set<Long> transactionIds = new HashSet<>();
        String selectSql = "SELECT id FROM entities.\"Transaction\"";

        try (PreparedStatement statement = connection.prepareStatement(selectSql)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                transactionIds.add(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return !transactionIds.contains(transactionId);
    }
}
