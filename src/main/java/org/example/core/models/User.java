package org.example.core.models;

import lombok.Data;

/**
 * Класс, представляющий сущность пользователя
 */
@Data
public class User {
    /**
     * Уникальный идентификатор пользователя
     */
    private long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private float balance;

    /**
     * Статическая переменная, используемая для генерации уникальных id пользователей
     */
    private static long nextId = 1;

    /**
     * Конструктор для создания объекта пользователя
     *
     * @param username  Имя пользователя (логин) для входа
     * @param password  Пароль
     * @param firstName Имя
     * @param lastName  Фамилия
     * @param balance   Начальный баланс
     */
    public User(String username, String password, String firstName, String lastName, float balance) {
        this.id = nextId++;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.balance = balance;
    }
}

