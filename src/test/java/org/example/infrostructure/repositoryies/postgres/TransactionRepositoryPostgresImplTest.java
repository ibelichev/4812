package org.example.infrostructure.repositoryies.postgres;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.example.core.models.Transaction;
import org.example.core.models.enums.AuditableStatus;
import org.example.core.models.enums.TransactionType;
import org.example.core.repositories.TransactionRepository;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


public class TransactionRepositoryPostgresImplTest {

    @ClassRule
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test")
            .waitingFor(Wait.forListeningPort());

    private TransactionRepository transactionRepository;

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

        transactionRepository = new TransactionRepositoryPostgresImpl(connection);
    }

    @Test
    public void testAddTransaction() {
        Transaction transaction = new Transaction(2, LocalDateTime.now(),
                TransactionType.CREDIT, AuditableStatus.SUCCESS, 123f);
        transactionRepository.addTransaction(transaction);
        List<Transaction> transactions = transactionRepository.getAll();
        assertThat(transactions).isNotEmpty();
        assertThat(transactions).contains(transaction);
    }

    @Test
    public void testFindById() {
        Transaction transaction = new Transaction(2, LocalDateTime.now(),
                TransactionType.CREDIT, AuditableStatus.SUCCESS, 321);
        Transaction transaction1 = new Transaction(2, LocalDateTime.now(),
                TransactionType.CREDIT, AuditableStatus.SUCCESS, 123);
        Transaction transaction2 = new Transaction(3, LocalDateTime.now(),
                TransactionType.CREDIT, AuditableStatus.SUCCESS, 123);
        transactionRepository.addTransaction(transaction);
        transactionRepository.addTransaction(transaction1);
        transactionRepository.addTransaction(transaction2);
        Transaction found = transactionRepository.findById(transaction.getId());
        Transaction found1 = transactionRepository.findById(234);
        assertThat(found).isEqualTo(transaction);
        assertThat(found1).isNull();
    }

    @Test
    public void testFindAllByUserId() {
        long userId = 1;
        Transaction transaction = new Transaction(userId, LocalDateTime.now(),
                TransactionType.CREDIT, AuditableStatus.SUCCESS, 321);
        Transaction transaction1 = new Transaction(userId, LocalDateTime.now(),
                TransactionType.CREDIT, AuditableStatus.SUCCESS, 123);
        Transaction transaction2 = new Transaction(2, LocalDateTime.now(),
                TransactionType.CREDIT, AuditableStatus.SUCCESS, 123);
        transactionRepository.addTransaction(transaction);
        transactionRepository.addTransaction(transaction1);
        transactionRepository.addTransaction(transaction2);

        List<Transaction> foundTransactions = transactionRepository.findAllByUserId(userId);
        assertThat(foundTransactions).containsExactlyInAnyOrder(transaction, transaction1);
    }

    @Test
    public void testGetAll() {
        long userId = 1;
        Transaction transaction = new Transaction(userId, LocalDateTime.now(),
                TransactionType.CREDIT, AuditableStatus.SUCCESS, 321);
        Transaction transaction1 = new Transaction(userId, LocalDateTime.now(),
                TransactionType.CREDIT, AuditableStatus.SUCCESS, 123);
        Transaction transaction2 = new Transaction(2, LocalDateTime.now(),
                TransactionType.CREDIT, AuditableStatus.SUCCESS, 123);
        transactionRepository.addTransaction(transaction);
        transactionRepository.addTransaction(transaction1);
        transactionRepository.addTransaction(transaction2);

        List<Transaction> foundTransactions = transactionRepository.getAll();
        assertThat(foundTransactions).containsExactlyInAnyOrder(transaction, transaction1, transaction2);
    }
}