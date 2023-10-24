package org.example.infrostructure.repositoryies.postgres;

import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.example.core.models.User;
import org.example.core.repositories.UserRepository;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserRepositoryPostgresTest {

    @ClassRule
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test")
            .waitingFor(Wait.forListeningPort());

    private UserRepository userRepository;

    @Before
    public void setUp() throws SQLException {
        Connection connection = DriverManager.getConnection(
                postgresContainer.getJdbcUrl(),
                postgresContainer.getUsername(),
                postgresContainer.getPassword()
        );

        try {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));

            Liquibase liquibase = new Liquibase("db/changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update();
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        } catch (LiquibaseException e) {
            throw new RuntimeException(e);
        }

        userRepository = new UserRepositoryPostgresImpl(connection);
    }

    @Test
    public void testAddUser() {
        User user = new User("u1", "p1", "f1", "l1", 123f);
        userRepository.addUser(user);

        User retrievedUser = userRepository.findByUsername("u1");

        assertThat(retrievedUser).isNotNull();
        assertThat(retrievedUser).isEqualTo(user);
    }

    @Test
    public void testDeleteUser() {
        User user = new User("u1", "p1", "f1", "l1", 123f);
        User user1 = new User("u2", "p1", "f1", "l1", 123f);
        userRepository.addUser(user);
        userRepository.addUser(user1);
        userRepository.deleteUser(user);

        User foundUser = userRepository.findByUsername(user.getUsername());
        User foundUser1 = userRepository.findByUsername(user1.getUsername());
        assertThat(foundUser).isNull();
        assertThat(foundUser1).isNotNull();
    }

    @Test
    public void testUpdateUser() {
        User user = new User("u1", "p1", "f1", "l1", 123f);
        userRepository.addUser(user);
        user.setBalance(32111f);
        userRepository.updateUser(user);

        User updatedUser = userRepository.findByUsername(user.getUsername());
        assertThat(updatedUser).isEqualTo(user);
    }

    @Test
    public void testFindById() {
        User user = new User("u1", "p1", "f1", "l1", 123f);
        User user1 = new User("u2", "p2", "f2", "l2", 123123f);
        User user2 = new User("u2", "p2", "f2", "l2", 123123f);
        userRepository.addUser(user);
        userRepository.addUser(user1);
        userRepository.addUser(user2);

        assertThat(user.getId()).isNotNull();

        User foundUser = userRepository.findById(user.getId());
        assertThat(foundUser).isEqualTo(user);
    }

    @Test
    public void testFinByUsername() {
        User user = new User("u1", "p1", "f1", "l1", 123f);
        User user1 = new User("u2", "p2", "f2", "l2", 123123f);
        User user2 = new User("u3", "p3", "f3", "l3", 12323f);
        userRepository.addUser(user);
        userRepository.addUser(user1);
        userRepository.addUser(user2);

        User foundUser = userRepository.findByUsername(user.getUsername());
        assertThat(foundUser).isEqualTo(user);
    }

    @Test
    public void testFindAll() {
        User user = new User("u1", "p1", "f1", "l1", 123f);
        User user1 = new User("u2", "p2", "f2", "l2", 123123f);
        User user2 = new User("u3", "p3", "f3", "l3", 12323f);
        userRepository.addUser(user);
        userRepository.addUser(user1);
        userRepository.addUser(user2);

        List<User> foundUsers = userRepository.findAll();
        assertThat(foundUsers).containsExactlyInAnyOrder(user, user1, user2);
    }

}


