package org.example.infrostructure.repositoryies.collections;

import lombok.NoArgsConstructor;
import org.example.core.models.User;
import org.example.core.repositories.UserRepository;

import java.util.*;

/**
 * Реализация интерфейса {@link UserRepository},
 * предоставляющая функциональность для работы с пользователями в системе
 */
@NoArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final List<User> users = new ArrayList<>();
    private long nextUserId = 1;

    @Override
    public void addUser(User user) {
        user.setId(nextUserId++);
        users.add(user);
    }

    @Override
    public void deleteUser(User user) {
        users.remove(user);
    }

    @Override
    public void updateUser(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == user.getId()) {
                users.set(i, user);
                return;
            }
        }
    }

    @Override
    public User findById(long id) {
        Optional<User> user = users.stream().filter(u -> u.getId() == id).findFirst();
        return user.orElse(null);
    }

    @Override
    public User findByUsername(String username) {
        Optional<User> userOptional = users.stream().filter(u -> u.getUsername().equals(username)).findFirst();
        return userOptional.orElse(null);
    }

    @Override
    public List<User> findAll() {
        return users;
    }
}
