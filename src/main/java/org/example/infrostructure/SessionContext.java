package org.example.infrostructure;

import org.example.core.models.User;

/**
 * Класс для управления текущей сессией пользователя
 */
public class SessionContext {
    private static User loggedInUser;

    /**
     * Получить залогиненного пользователя
     *
     * @return Залогиненный пользователь или null, если никто не вошел в систему
     */
    public static User getLoggedInUser() {
        return loggedInUser;
    }

    /**
     * Задать текущего залогиненного пользователя
     *
     * @param user Залогиненный пользователь
     */
    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }

    /**
     * Очистить информацию о залогиненном пользователе
     */
    public static void clearLoggedInUser() {
        loggedInUser = null;
    }
}
