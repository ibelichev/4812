package org.example.infrostructure.services;

import lombok.RequiredArgsConstructor;
import org.example.core.models.Action;
import org.example.core.models.User;
import org.example.core.models.enums.ActionType;
import org.example.core.models.enums.AuditableStatus;
import org.example.core.repositories.AuditableRepository;
import org.example.core.repositories.UserRepository;
import org.example.infrostructure.SessionContext;

import java.time.LocalDateTime;

/**
 * Сервис, отвечающий за аутентификацию и регистрацию пользователей.
 */
@RequiredArgsConstructor
public class AuthorisationService {
    private final UserRepository userRepository;
    private final AuditableRepository auditableRepository;

    /**
     * Перечисление для статусов авторизации.
     */
    public enum AuthorisationStatus {
        SUCCESS,
        USER_NOT_FOUND,
        INVALID_PASSWORD
    }

    /**
     * Метод для авторизации пользователя, задает пользователя в {@link SessionContext}
     *
     * @param username Имя пользователя для входа.
     * @param password Пароль пользователя.
     * @return Статус авторизации (SUCCESS, USER_NOT_FOUND, или INVALID_PASSWORD).
     */
    public AuthorisationStatus authorisation(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            if (user.getPassword().equals(password)) {
                SessionContext.setLoggedInUser(user);

                LocalDateTime dateTime = LocalDateTime.now();
                AuditableStatus status = AuditableStatus.SUCCESS;
                ActionType actionType = ActionType.LOGIN;

                Action action = new Action(user.getId(), dateTime, status, actionType);
                auditableRepository.addAuditable(action);

                return AuthorisationService.AuthorisationStatus.SUCCESS;
            } else {
                return AuthorisationService.AuthorisationStatus.INVALID_PASSWORD;
            }
        } else {
            return AuthorisationService.AuthorisationStatus.USER_NOT_FOUND;
        }
    }

    /**
     * Метод для регистрации нового пользователя.
     *
     * @param user Пользователь для регистрации.
     * @return true, если регистрация прошла успешно, false в противном случае.
     */
    public boolean registration(User user) {
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null) {
            return false; // Пользователь с таким именем уже существует.
        }
        userRepository.addUser(user);

        LocalDateTime dateTime = LocalDateTime.now();
        AuditableStatus status = AuditableStatus.SUCCESS;
        ActionType actionType = ActionType.REGISTRATION;

        Action action = new Action(user.getId(), dateTime, status, actionType);
        auditableRepository.addAuditable(action);

        return true;
    }


    /**
     * Метод для завершения сессии, удаляет текущего пользователя из {@link SessionContext}
     */
    public void logout(User user) {
        LocalDateTime dateTime = LocalDateTime.now();
        AuditableStatus status = AuditableStatus.SUCCESS;
        ActionType actionType = ActionType.LOGOUT;

        Action action = new Action(user.getId(), dateTime, status, actionType);
        auditableRepository.addAuditable(action);

        SessionContext.clearLoggedInUser();
    }
}
