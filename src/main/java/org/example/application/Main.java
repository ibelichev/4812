package org.example.application;

import org.example.core.repositories.AuditableRepository;
import org.example.core.repositories.TransactionRepository;
import org.example.core.repositories.UserRepository;
import org.example.core.services.OperationService;
import org.example.core.services.UserService;
import org.example.infrostructure.in.console.ConsoleUI;
import org.example.infrostructure.repositoryies.AuditableReppositoryImpl;
import org.example.infrostructure.repositoryies.TransactionRepositoryImpl;
import org.example.infrostructure.repositoryies.UserRepositoryImpl;
import org.example.infrostructure.services.AuthorisationService;
import org.example.infrostructure.services.OperationServiceImpl;
import org.example.infrostructure.services.UserServiceImpl;



/**
 * Главный класс приложения, отвечающий за запуск приложения и настройку всех его компонентов
 */
public class Main {
    public static void main(String[] args) {
        // Создание репозиториев
        UserRepository userRepository = new UserRepositoryImpl();
        TransactionRepository transactionRepository = new TransactionRepositoryImpl();
        AuditableRepository auditableRepository = new AuditableReppositoryImpl();

        // Создание сервисов
        OperationService operationService = new OperationServiceImpl(userRepository, transactionRepository, auditableRepository);
        UserService userService = new UserServiceImpl(userRepository);
        AuthorisationService authorisationService = new AuthorisationService(userRepository, auditableRepository);

        // Создание и запуск консольного пользовательского интерфейса
        ConsoleUI consoleUI = new ConsoleUI(authorisationService, userService, operationService);
        consoleUI.start();
    }
}