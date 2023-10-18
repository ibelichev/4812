package org.example.infrostructure.repositoryies.postgres;

import org.example.core.models.Action;
import org.example.core.models.Auditable;
import org.example.core.models.Transaction;
import org.example.core.models.enums.ActionType;
import org.example.core.models.enums.AuditableStatus;
import org.example.core.models.enums.TransactionType;
import org.example.core.repositories.AuditableRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Реализация интерфейса {@link AuditableRepository},
 * через PostgreSql
 * предоставляющая функциональность для работы с
 * наследниками {@link Auditable}
 * в системе.
 */
public class AuditableRepositoryPostgresImpl implements AuditableRepository {
    private final Connection connection;

    public AuditableRepositoryPostgresImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addAuditable(Auditable auditable) {
        String insertSql;
        if (auditable instanceof Transaction) {
            // Определение SQL-запроса для таблицы Transaction
            insertSql = "INSERT INTO entities.\"Auditable\" (user_id, date_time, status, type, amount) VALUES (?, ?, ?, ?, ?) RETURNING id";
        } else if (auditable instanceof Action) {
            // Определение SQL-запроса для таблицы Action
            insertSql = "INSERT INTO entities.\"Auditable\" (user_id, date_time, status, action_type) VALUES (?, ?, ?, ?) RETURNING id";
        } else {
            throw new IllegalArgumentException("Unsupported Auditable type.");
        }

        try (PreparedStatement statement = connection.prepareStatement(insertSql)) {
            // Заполнение параметров SQL-запроса в зависимости от типа Auditable
            statement.setLong(1, auditable.getUserId());
            statement.setObject(2, auditable.getDateTime());
            statement.setString(3, auditable.getStatus().toString());
            if (auditable instanceof Transaction) {
                Transaction transaction = (Transaction) auditable;
                statement.setString(4, transaction.getType().toString());
                statement.setFloat(5, transaction.getAmount());
            } else if (auditable instanceof Action) {
                Action action = (Action) auditable;
                statement.setString(4, action.getType().toString());
            }

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                long id = resultSet.getLong(1);
                auditable.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Auditable findById(long id) {
        String selectSql = "SELECT * FROM entities.\"Auditable\" WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(selectSql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                long userId = resultSet.getLong("user_id");
                LocalDateTime dateTime = resultSet.getObject("date_time", LocalDateTime.class);
                AuditableStatus status = AuditableStatus.valueOf(resultSet.getString("status"));

                if (resultSet.getString("type") != null) {
                    TransactionType type = TransactionType.valueOf(resultSet.getString("type"));
                    float amount = resultSet.getFloat("amount");
                    return new Transaction(id, userId, dateTime, type, status, amount);
                } else if (resultSet.getString("action_type") != null) {
                    ActionType actionType = ActionType.valueOf(resultSet.getString("action_type"));
                    return new Action(id, userId, dateTime, status, actionType);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Auditable> findAllByUserId(long userId) {
        List<Auditable> userAuditables = new ArrayList<>();
        String selectSql = "SELECT * FROM entities.\"Auditable\" WHERE user_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(selectSql)) {
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                LocalDateTime dateTime = resultSet.getObject("date_time", LocalDateTime.class);
                AuditableStatus status = AuditableStatus.valueOf(resultSet.getString("status"));

                if (resultSet.getString("type") != null) {
                    TransactionType type = TransactionType.valueOf(resultSet.getString("type"));
                    float amount = resultSet.getFloat("amount");
                    userAuditables.add(new Transaction(id, userId, dateTime, type, status, amount));
                } else if (resultSet.getString("action_type") != null) {
                    ActionType actionType = ActionType.valueOf(resultSet.getString("action_type"));
                    userAuditables.add(new Action(id, userId, dateTime, status, actionType));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userAuditables;
    }

    @Override
    public List<Auditable> getAll() {
        List<Auditable> audits = new ArrayList<>();
        String selectSql = "SELECT * FROM entities.\"Auditable\"";

        try (PreparedStatement statement = connection.prepareStatement(selectSql)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                long userId = resultSet.getLong("user_id");
                LocalDateTime dateTime = resultSet.getObject("date_time", LocalDateTime.class);
                AuditableStatus status = AuditableStatus.valueOf(resultSet.getString("status"));

                if (resultSet.getString("type") != null) {
                    TransactionType type = TransactionType.valueOf(resultSet.getString("type"));
                    float amount = resultSet.getFloat("amount");
                    audits.add(new Transaction(id, userId, dateTime, type, status, amount));
                } else if (resultSet.getString("action_type") != null) {
                    ActionType actionType = ActionType.valueOf(resultSet.getString("action_type"));
                    audits.add(new Action(id, userId, dateTime, status, actionType));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return audits;
    }
}
