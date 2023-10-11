package org.example.core.repositories;

import org.example.core.models.User;
import java.util.List;

/**
 * Интерфейс, представляющий репозиторий для пользователей
 * {@link User}
 */
public interface UserRepository {

    /**
     * Добавляет пользователя в репозиторий
     *
     * @param user Пользователь для добавления
     */
    void addUser(User user);

    /**
     * Удаляет пользователя из репозитория
     *
     * @param user Пользователь для удаления
     */
    void deleteUser(User user);

    /**
     * Обновляет информацию о пользователе в репозитории
     *
     * @param user Пользователь с обновленными данными
     */
    void updateUser(User user);

    /**
     * Находит пользователя по его id
     *
     * @param id id пользователя
     * @return Пользователь, если найден, или null, если не найден
     */
    User findById(long id);

    /**
     * Находит пользователя по его имени пользователя (логину)
     *
     * @param username Имя пользователя для поиска
     * @return Пользователь, если найден, или null, если не найден
     */
    User findByUsername(String username);

    /**
     * Возвращает список всех пользователей из репозитория
     *
     * @return Список всех пользователей
     */
    List<User> findAll();
}
