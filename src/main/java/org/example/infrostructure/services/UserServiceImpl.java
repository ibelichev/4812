package org.example.infrostructure.services;

import lombok.RequiredArgsConstructor;
import org.example.core.models.User;
import org.example.core.repositories.UserRepository;
import org.example.core.services.UserService;

import java.util.List;

/**
 * Реализация интерфейса {@link UserService}
 */
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public void addUser(User user) {
        userRepository.addUser(user);
    }

    @Override
    public void deleteUser(User user) {
        userRepository.deleteUser(user);
    }

    @Override
    public User findById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findAll() {
       return userRepository.findAll();
    }
}
