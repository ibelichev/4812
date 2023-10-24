package org.example.infrostructure.repositoryies.postgres;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.example.core.models.Action;
import org.example.core.models.Auditable;
import org.example.core.models.enums.ActionType;
import org.example.core.models.enums.AuditableStatus;
import org.example.core.repositories.AuditableRepository;
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
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


public class AuditableRepositoryPostgresImplTest {
    @ClassRule
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test")
            .waitingFor(Wait.forListeningPort());

    private AuditableRepository auditableRepository;

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

        auditableRepository = new AuditableRepositoryPostgresImpl(connection);
    }

    @Test
    public void testAddAuditable() {
        Auditable action = new Action(2, LocalDateTime.now(), AuditableStatus.SUCCESS, ActionType.LOGIN);
        auditableRepository.addAuditable(action);
        List<Auditable> list = auditableRepository.getAll();
        assertThat(list).contains(action);
        assertThat(list).isNotEmpty();
    }

    @Test
    public void testFindById() {
        Auditable action = new Action(2, LocalDateTime.now(), AuditableStatus.SUCCESS, ActionType.LOGIN);
        Auditable action1 = new Action(1, LocalDateTime.now(), AuditableStatus.SUCCESS, ActionType.LOGIN);
        Auditable action2 = new Action(2, LocalDateTime.now(), AuditableStatus.SUCCESS, ActionType.LOGIN);
        auditableRepository.addAuditable(action);
        auditableRepository.addAuditable(action1);
        Auditable foundAction = auditableRepository.findById(action.getId());
        Auditable foundActionNull = auditableRepository.findById(action2.getId());
        assertThat(foundAction).isEqualTo(action);
        assertThat(foundActionNull).isNull();
    }

    @Test
    public void testFindAllByUserId() {
        long userId = 1;
        Auditable action = new Action(userId, LocalDateTime.now(), AuditableStatus.SUCCESS, ActionType.LOGIN);
        Auditable action1 = new Action(userId, LocalDateTime.now(), AuditableStatus.SUCCESS, ActionType.LOGIN);
        Auditable action2 = new Action(2, LocalDateTime.now(), AuditableStatus.SUCCESS, ActionType.LOGIN);
        auditableRepository.addAuditable(action);
        auditableRepository.addAuditable(action1);
        auditableRepository.addAuditable(action2);
        List<Auditable> found = auditableRepository.findAllByUserId(userId);
        List<Auditable> expected = new ArrayList<>();
        expected.add(action);
        expected.add(action1);
        assertThat(found).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    public void findAllByUserIdEmptyTest() {
        long userId = 1;
        Auditable action = new Action(userId, LocalDateTime.now(), AuditableStatus.SUCCESS, ActionType.LOGIN);
        Auditable action1 = new Action(userId, LocalDateTime.now(), AuditableStatus.SUCCESS, ActionType.LOGIN);
        Auditable action2 = new Action(2, LocalDateTime.now(), AuditableStatus.SUCCESS, ActionType.LOGIN);
        auditableRepository.addAuditable(action);
        auditableRepository.addAuditable(action1);
        auditableRepository.addAuditable(action2);
        List<Auditable> found = auditableRepository.findAllByUserId(5);
        assertThat(found).isEmpty();
    }

    @Test
    public void testGetAll() {
        long userId = 1;
        Auditable action = new Action(userId, LocalDateTime.now(), AuditableStatus.SUCCESS, ActionType.LOGIN);
        Auditable action1 = new Action(userId, LocalDateTime.now(), AuditableStatus.SUCCESS, ActionType.LOGIN);
        Auditable action2 = new Action(2, LocalDateTime.now(), AuditableStatus.SUCCESS, ActionType.LOGIN);
        auditableRepository.addAuditable(action);
        auditableRepository.addAuditable(action1);
        auditableRepository.addAuditable(action2);

        List<Auditable> found = auditableRepository.getAll();
        assertThat(found).containsExactlyInAnyOrder(action, action1, action2);
    }
}