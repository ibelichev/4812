package org.example.core.services;

import org.example.core.models.User;
import org.example.core.repositories.UserRepository;

import java.util.List;

/**
 * Сервис для выполнения обращений к {@link UserRepository}
 * и изоляции слоя репозитория от слоев, взаимодействующих с инфраструктурой
 */
public interface UserService {

    /**
     * Добавляет нового пользователя
     *
     * @param user Пользователь для добавления
     */
    void addUser(User user);

    /**
     * Удаляет пользователя
     *
     * @param user Пользователь для удаления
     */
    void deleteUser(User user);

    /**
     * Находит пользователя по его id
     *
     * @param id id пользователя
     * @return Найденный пользователь или null, если пользователь не найден
     */
    User findById(long id);

    /**
     * Находит пользователя по его имени пользователя (логину)
     *
     * @param username Имя пользователя
     * @return Найденный пользователь или null, если пользователь с указанным именем не найден
     */
    User findByUsername(String username);

    /**
     * Получает список всех пользователей
     *
     * @return Список всех пользователей
     */
    List<User> findAll();
}
