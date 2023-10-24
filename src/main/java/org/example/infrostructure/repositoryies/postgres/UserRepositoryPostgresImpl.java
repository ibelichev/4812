package org.example.infrostructure.repositoryies.postgres;

import org.example.core.models.User;
import org.example.core.repositories.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Реализация интерфейса {@link UserRepository},
 * предоставляющая функциональность для работы с пользователями в системе,
 * через PostgreSql
 */
public class UserRepositoryPostgresImpl implements UserRepository {
    private final Connection connection;

    public UserRepositoryPostgresImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addUser(User user) {
        String insertSql = "INSERT INTO entities.\"User\" (username, password, first_name, last_name, balance) VALUES (?, ?, ?, ?, ?) RETURNING id";

        try (PreparedStatement statement = connection.prepareStatement(insertSql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getFirstName());
            statement.setString(4, user.getLastName());
            statement.setFloat(5, user.getBalance());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                long id = resultSet.getLong(1);
                user.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void deleteUser(User user) {
        String deleteSql = "DELETE FROM entities.\"User\" WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(deleteSql)) {
            statement.setLong(1, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateUser(User user) {
        String updateSql = "UPDATE entities.\"User\" SET balance = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(updateSql)) {
            statement.setFloat(1, user.getBalance());
            statement.setLong(2, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User findById(long id) {
        String selectSql = "SELECT * FROM entities.\"User\" WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(selectSql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                float balance = resultSet.getFloat("balance");

                User user = new User(username, password, firstName, lastName, balance);
                user.setId(id);
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public User findByUsername(String username) {
        String selectSql = "SELECT * FROM entities.\"User\" WHERE username = ?";

        try (PreparedStatement statement = connection.prepareStatement(selectSql)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                long id = resultSet.getLong("id");
                String password = resultSet.getString("password");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                float balance = resultSet.getFloat("balance");

                User user = new User(username, password, firstName, lastName, balance);
                user.setId(id);
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String selectSql = "SELECT * FROM entities.\"User\"";

        try (PreparedStatement statement = connection.prepareStatement(selectSql)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                float balance = resultSet.getFloat("balance");

                User user = new User(username, password, firstName, lastName, balance);
                user.setId(id);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }
}
