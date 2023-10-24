package org.example.infrostructure.in.console;

import lombok.RequiredArgsConstructor;
import org.example.core.models.Action;
import org.example.core.models.Auditable;
import org.example.core.models.Transaction;
import org.example.core.models.User;
import org.example.core.models.enums.TransacionReturns;
import org.example.core.services.OperationService;
import org.example.core.services.UserService;
import org.example.infrostructure.SessionContext;
import org.example.infrostructure.services.AuthorisationService;


import java.util.List;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;


/**
 * Класс для интерфейса командной строки, обрабатывающий операции взаимодействия пользователя
 * с сервисами авторизации и операций, используя входные данные и текстовый интерфейс.
 */
@RequiredArgsConstructor
public class ConsoleUI {
    private final AuthorisationService authorisationService;
    private final UserService userService;
    private final OperationService operationService;


    private Scanner scanner = new Scanner(System.in);

    static String ascii =
            "\u001B[34m\n" +
                    "░██╗░░░░░░░██╗░█████╗░██╗░░░░░██╗░░░░░███████╗████████╗░░░░░░░██████╗███████╗██████╗░██╗░░░██╗██╗░█████╗░███████╗\n" +
                    "░██║░░██╗░░██║██╔══██╗██║░░░░░██║░░░░░██╔════╝╚══██╔══╝░░░░░░██╔════╝██╔════╝██╔══██╗██║░░░██║██║██╔══██╗██╔════╝\n" +
                    "░╚██╗████╗██╔╝███████║██║░░░░░██║░░░░░█████╗░░░░░██║░░░█████╗╚█████╗░█████╗░░██████╔╝╚██╗░██╔╝██║██║░░╚═╝█████╗░░\n" +
                    "░░████╔═████║░██╔══██║██║░░░░░██║░░░░░██╔══╝░░░░░██║░░░╚════╝░╚═══██╗██╔══╝░░██╔══██╗░╚████╔╝░██║██║░░██╗██╔══╝░░\n" +
                    "░░╚██╔╝░╚██╔╝░██║░░██║███████╗███████╗███████╗░░░██║░░░░░░░░░██████╔╝███████╗██║░░██║░░╚██╔╝░░██║╚█████╔╝███████╗\n" +
                    "░░░╚═╝░░░╚═╝░░╚═╝░░╚═╝╚══════╝╚══════╝╚══════╝░░░╚═╝░░░░░░░░░╚═════╝░╚══════╝╚═╝░░╚═╝░░░╚═╝░░░╚═╝░╚════╝░╚══════╝\u001B[0m";

    /**
     * Стартовый метод для входа в систему
     */
    public void start() {

        System.out.println(ascii);
        System.out.println("\n\n");

        while (true) {
            System.out.println("Войти / Зарегистрироваться (1 / 2)");
            System.out.print("Выберите действие: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> login();
                case 2 -> registration();
            }
        }
    }

    /**
     * Метод для выбора операции, доступен после входа в систему
     */
    private void operations() {
        System.out.println();
        User currentUser = SessionContext.getLoggedInUser();

        while (true) {
            System.out.printf("%s, ваш баланс: %f \n", currentUser.getUsername(), currentUser.getBalance());
            System.out.println("Пополнение (1) / Вывод (2) / История (3) / Аудит (4) / Выход (5): ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> credit(currentUser);
                case 2 -> debit(currentUser);
                case 3 -> history(currentUser);
                case 4 -> audit(currentUser);
                case 5 -> logout(currentUser);
            }
        }
    }

    /**
     * Метод для выполнения операций пополнения баланса текущего пользователя. Запрашивает
     * сумму для пополнения и передает ее сервису операций
     */
    private void credit(User currentUser) {
        scanner.nextLine();

        System.out.print("Введите сумму пополнения: ");
        float amount = scanner.nextFloat();
        TransacionReturns status = operationService.credit(currentUser, amount);
        if (status == TransacionReturns.SUCCESS) {
            System.out.println("Операция выполнена успешно");
        } else if (status == TransacionReturns.UNUNIQUE_ID) {
            throw new IllegalArgumentException("Идентификатор транзакции уже существует");
        }
        System.out.println();

    }

    /**
     * Метод для выполнения операций снятия средств с баланса текущего пользователя.
     * Запрашивает сумму для снятия и передает ее сервису операций.
     */
    private void debit(User currentUser) {
        scanner.nextLine();

        System.out.print("Введите сумму снятия: ");
        float amount = scanner.nextFloat();
        TransacionReturns status = operationService.debit(currentUser, amount);
        if (status == TransacionReturns.SUCCESS) {
            System.out.println("Операция выполнена успешно");
        } else if (status == TransacionReturns.NOT_ENOUGH_MONEY) {
            System.out.println("Недостаточно средств на балансе");
        } else if (status == TransacionReturns.UNUNIQUE_ID) {
            throw new IllegalArgumentException("Идентификатор транзакции уже существует");
        }
        System.out.println();
    }


    /**
     * Метод для отображения истории транзакций текущего пользователя.
     * Выводит дату и время, тип, сумму и статус каждой транзакции
     */
    private void history(User currentUser) {
        System.out.println("История операций:");
        List<Transaction> transactions = operationService.history(currentUser);

        if (transactions.isEmpty()) {
            System.out.println("Вы еще не совершали никаких транзакций");
        } else {
            // Вывод заголовка таблицы
            System.out.printf("%-5s %-20s %-20s %-15s %-10s%n",
                    "ID", "Дата и время", "Тип", "Сумма", "Статус"
            );

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // Вывод данных о транзакциях
            for (Transaction transaction : transactions) {
                String formattedDateTime = transaction.getDateTime().format(formatter);
                System.out.printf("%-5d %-20s %-20s %-15.4f %-15s%n",
                        transaction.getId(),
                        formattedDateTime,
                        transaction.getType().toString(),
                        transaction.getAmount(),
                        transaction.getStatus().toString()
                );
            }
        }
        System.out.println();
    }


    /**
     * Метод для отображения аудита действий текущего пользователя.
     * Выводит дату и время, тип, статус и тип операции (действие или транзакция)
     */
    private void audit(User currentUser) {
        System.out.println("Аудит действий пользователя:");
        List<Auditable> audits = operationService.audit(currentUser);

        System.out.printf("%-5s %-20s %-15s %-15s %-15s%n",
                "ID", "Дата и время", "Тип", "Статус", "Тип операции"
        );

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (Auditable audit : audits) {
            String formattedDateTime = audit.getDateTime().format(formatter);
            String type = audit instanceof Transaction ? "Транзакция" : "Действие";
            System.out.printf("%-5d %-20s %-15s %-15s %-15s%n",
                    audit.getId(),
                    formattedDateTime,
                    type,
                    audit.getStatus().toString(),
                    audit instanceof Transaction ? ((Transaction)
                            audit).getType().toString() : ((Action) audit).getType().toString()
            );
        }
        System.out.println();
    }




    /**
     * Метод для входа в систему, запрашивает логин и пароль, а затем передает их
     * сервису авторизации для проверки. Пользователю предоставляется доступ к операциям
     * после успешной авторизации
     */
    private void login() {
        scanner.nextLine();

        System.out.print("Введите логин: ");
        String username = scanner.nextLine();

        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();
        AuthorisationService.AuthorisationStatus status =
                authorisationService.authorisation(username, password);

        if (status == AuthorisationService.AuthorisationStatus.SUCCESS) {
            operations();

        } else if (status == AuthorisationService.AuthorisationStatus.USER_NOT_FOUND) {
            System.out.println("Пользователь с таким логином не найден");

        } else if (status == AuthorisationService.AuthorisationStatus.INVALID_PASSWORD) {
            System.out.println("Неправильный пароль");

        }
        System.out.println();
    }


    /**
     * Метод для регистрации нового пользователя. Запрашивает информацию о новом пользователе,
     * включая логин, пароль, имя и фамилию, и передает эту информацию сервису авторизации
     * для создания новой учетной записи
     */
    private void registration() {
        scanner.nextLine();

        System.out.print("Введите логин: ");
        String username = scanner.nextLine();

        System.out.print("Введите пароль: ");
        String password1 = scanner.nextLine();

        System.out.print("Подтвердите пароль: ");
        String password2 = scanner.nextLine();

        if (username.isEmpty() || password1.isEmpty()) {
            System.out.println("Логин и пароль должны быть заполнены. Попробуйте ещё раз.");
            return;
        }

        if (!password1.equals(password2)) {
            System.out.println("Пароли не совпадают. Попробуйте ещё раз.");
            return;
        }

        System.out.print("Введите имя: ");
        String firstName = scanner.nextLine();

        System.out.print("Введите фамилию: ");
        String lastName = scanner.nextLine();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            System.out.println("Имя и фамилия должны быть заполнены. Попробуйте ещё раз.");
            return;
        }

        User newUser = new User(username, password1, firstName, lastName, 0.0f);
        boolean status = authorisationService.registration(newUser);
        if (status) {
            System.out.println("Регистрация успешно завершена.");
        } else {
            System.out.println("Регистрация не удалась. Попробуйте ещё раз.");
        }
        System.out.println();
    }

    /**
     * Метод для завершения сеанса пользователя и выхода из системы.
     * Вызывает сервис авторизации для выхода и возвращает пользователя на стартовый экран
     */
    private void logout(User currentUser) {
        System.out.println("До свидания!");
        authorisationService.logout(currentUser);
        System.out.println();
        start();
    }


    /**
     * Метод для очистки содержимого консоли, создавая пустой экран
     */
    private void clear() {
        for(int i = 0; i < 80 * 300; i++) // Default Height of cmd is 300 and Default width is 80
            System.out.print("\b");
    }
}
