package org.example.infrostructure.repositoryies.collections;

import org.example.core.models.User;
import org.example.core.repositories.UserRepository;
import org.example.infrostructure.repositoryies.collections.UserRepositoryImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class UserRepositoryImplTest {

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepositoryImpl();
    }

    @Test
    void addUserTest() {
        User user = new User("u1", "p1", "i", "b", 100);
        userRepository.addUser(user);
        List<User> users = userRepository.findAll();
        assertThat(users).isNotEmpty();
        assertThat(users).contains(user);
    }

    @Test
    void findByIdTest() {
        long id1 = 1;
        long id2 = 2;
        User user1 = new User(id1, "u1", "p1", "n1", "l1", 200);
        User user2 = new User(id2, "u2", "p2", "n2", "l2", 300.389f);
        userRepository.addUser(user1);
        userRepository.addUser(user2);
        User foundUser1 = userRepository.findById(id1);
        User foundUser2 = userRepository.findById(id2);
        assertThat(foundUser1).isEqualTo(user1);
        assertThat(foundUser2).isEqualTo(user2);
    }

    @Test
    void findByUsernameTest() {
        User user1 = new User("u1", "p1", "n1", "l1", 200);
        User user2 = new User("u2", "p2", "n2", "l2", 300.389f);
        userRepository.addUser(user1);
        userRepository.addUser(user2);
        User foundUser1 = userRepository.findByUsername(user1.getUsername());
        User foundUser2 = userRepository.findByUsername(user2.getUsername());
        assertThat(foundUser1).isEqualTo(user1);
        assertThat(foundUser2).isEqualTo(user2);
    }

    @Test
    void updateUserTest() {
        User user = new User("u1", "p1", "n1", "l1", 200);
        userRepository.addUser(user);
        user.setFirstName("UpdatedFirstName");
        user.setLastName("UpdatedLastName");
        userRepository.updateUser(user);
        User updatedUser = userRepository.findById(user.getId());
        assertThat(updatedUser).isEqualTo(user);
    }

    @Test
    void deleteUserTest() {
        User user = new User("u1", "p1", "n1", "l1", 200);
        userRepository.addUser(user);
        userRepository.deleteUser(user);
        User foundUser = userRepository.findById(user.getId());
        assertThat(foundUser).isNull();
    }
}
