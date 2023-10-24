package org.example.infrostructure.services;

import org.example.core.models.Action;
import org.example.core.models.User;
import org.example.core.models.enums.ActionType;
import org.example.core.models.enums.AuditableStatus;
import org.example.core.repositories.AuditableRepository;
import org.example.core.repositories.UserRepository;
import org.example.infrostructure.SessionContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthorisationServiceTest {

    private AuthorisationService authorisationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuditableRepository auditableRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authorisationService = new AuthorisationService(userRepository, auditableRepository);
    }

    @Test
    void testAuthorisationSuccess() {
        User user = new User("username", "password", "f1", "l1", 100);
        when(userRepository.findByUsername("username")).thenReturn(user);

        LocalDateTime dateTime = LocalDateTime.now();
        AuditableStatus status = AuditableStatus.SUCCESS;
        ActionType actionType = ActionType.LOGIN;

        AuthorisationService.AuthorisationStatus statusResult = authorisationService.authorisation("username", "password");

        assertEquals(AuthorisationService.AuthorisationStatus.SUCCESS, statusResult);
        assertEquals(user, SessionContext.getLoggedInUser());
        verify(auditableRepository).addAuditable(any(Action.class));
    }

    @Test
    void testAuthorisationUserNotFound() {
        when(userRepository.findByUsername("username")).thenReturn(null);

        AuthorisationService.AuthorisationStatus statusResult = authorisationService.authorisation("username", "password");

        assertEquals(AuthorisationService.AuthorisationStatus.USER_NOT_FOUND, statusResult);
        assertEquals(null, SessionContext.getLoggedInUser());
        verify(auditableRepository, Mockito.never()).addAuditable(any(Action.class));
    }

    @Test
    void testAuthorisationInvalidPassword() {
        User user = new User("username", "password", "f1", "l1", 100);
        when(userRepository.findByUsername("username")).thenReturn(user);

        AuthorisationService.AuthorisationStatus statusResult = authorisationService.authorisation("username", "wrong_password");

        assertEquals(AuthorisationService.AuthorisationStatus.INVALID_PASSWORD, statusResult);
        assertEquals(null, SessionContext.getLoggedInUser());
        verify(auditableRepository, Mockito.never()).addAuditable(any(Action.class));
    }

    @Test
    void testRegistrationSuccess() {
        User user = new User("new_user", "password", "f1", "l1", 200);
        when(userRepository.findByUsername("new_user")).thenReturn(null);

        LocalDateTime dateTime = LocalDateTime.now();
        AuditableStatus status = AuditableStatus.SUCCESS;
        ActionType actionType = ActionType.REGISTRATION;

        boolean registrationResult = authorisationService.registration(user);

        assertTrue(registrationResult);
        verify(userRepository).addUser(user);
        verify(auditableRepository).addAuditable(any(Action.class));
    }

    @Test
    void testRegistrationUserAlreadyExists() {
        User existingUser = new User("existing_user", "password", "u1", "l1", 200);
        when(userRepository.findByUsername("existing_user")).thenReturn(existingUser);

        User user = new User("existing_user", "password", "u1", "l1", 200);

        boolean registrationResult = authorisationService.registration(user);

        assertTrue(!registrationResult);
        verify(userRepository, Mockito.never()).addUser(user);
        verify(auditableRepository, Mockito.never()).addAuditable(any(Action.class));
    }

    @Test
    void testLogout() {
        User user = new User("username", "password", "f", "l", 100);
        LocalDateTime dateTime = LocalDateTime.now();
        AuditableStatus status = AuditableStatus.SUCCESS;
        ActionType actionType = ActionType.LOGOUT;
        Action action = new Action(1, user.getId(), dateTime, status, actionType);

        SessionContext.setLoggedInUser(user);

        authorisationService.logout(user);

        assertEquals(null, SessionContext.getLoggedInUser());
        verify(auditableRepository).addAuditable(action);
    }
}
