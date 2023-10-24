package org.example.application;

import org.example.core.repositories.AuditableRepository;
import org.example.core.repositories.TransactionRepository;
import org.example.core.repositories.UserRepository;
import org.example.core.services.OperationService;
import org.example.core.services.UserService;
import org.example.infrostructure.in.console.ConsoleUI;
import org.example.infrostructure.repositoryies.postgres.AuditableRepositoryPostgresImpl;
import org.example.infrostructure.repositoryies.postgres.TransactionRepositoryPostgresImpl;
import org.example.infrostructure.repositoryies.postgres.UserRepositoryPostgresImpl;
import org.example.infrostructure.services.AuthorisationService;
import org.example.infrostructure.services.OperationServiceImpl;
import org.example.infrostructure.services.UserServiceImpl;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


/**
 * Главный класс приложения, отвечающий за запуск приложения и настройку всех его компонентов
 */
public class Main {
    public static void main(String[] args) throws IOException, SQLException {

        // Создание подключение к бд
        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream("src/main/resources/application.properties");
        properties.load(fis);

        String url = properties.getProperty("url");
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");

        Connection connection = DriverManager.getConnection(url, username, password);


        // Создание репозиториев
        UserRepository userRepository = new UserRepositoryPostgresImpl(connection);
        TransactionRepository transactionRepository = new TransactionRepositoryPostgresImpl(connection);
        AuditableRepository auditableRepository = new AuditableRepositoryPostgresImpl(connection);


        // Создание сервисов
        OperationService operationService = new OperationServiceImpl(userRepository, transactionRepository, auditableRepository);
        UserService userService = new UserServiceImpl(userRepository);
        AuthorisationService authorisationService = new AuthorisationService(userRepository, auditableRepository);

        // Создание и запуск консольного пользовательского интерфейса
        ConsoleUI consoleUI = new ConsoleUI(authorisationService, userService, operationService);
        consoleUI.start();
    }
}