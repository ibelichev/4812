package org.example.infrostructure.services;

import org.example.core.models.User;
import org.example.core.repositories.UserRepository;
import org.example.core.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {
    private UserService userService;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void testAddUser() {
        User user = new User("user1", "password", "f1", "l1", 100);
        userService.addUser(user);
        verify(userRepository, times(1)).addUser(user);
    }

    @Test
    void testDeleteUser() {
        User user = new User("user1", "password", "f1", "l1", 100);
        userService.deleteUser(user);
        verify(userRepository, times(1)).deleteUser(user);
    }

    @Test
    void testFindById() {
        User user = new User("user1", "password", "f1", "l1", 100);
        long userId = user.getId();
        when(userRepository.findById(userId)).thenReturn(user);

        User foundUser = userService.findById(userId);

        assertThat(foundUser).isNotNull();
        assertThat(foundUser).isEqualTo(user);
    }

    @Test
    void testFindByUsername() {
        String username = "username";
        User user = new User("user1", "password", "f1", "l1", 100);
        when(userRepository.findByUsername(username)).thenReturn(user);

        User foundUser = userService.findByUsername(username);

        assertThat(foundUser).isNotNull();
        assertThat(foundUser).isEqualTo(user);
    }

    @Test
    void testFindAll() {
        List<User> users = List.of(
                new User("user1", "password", "f1", "l1", 100),
                new User("user2", "password", "f2", "l2", 200)
        );
        when(userRepository.findAll()).thenReturn(users);

        List<User> foundUsers = userService.findAll();

        assertThat(foundUsers).isNotEmpty();
        assertThat(foundUsers).containsExactlyInAnyOrderElementsOf(users);
    }
}
