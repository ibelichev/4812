package org.example.core.models;

import lombok.Data;
import org.example.core.models.enums.ActionType;
import org.example.core.models.enums.AuditableStatus;

import java.time.LocalDateTime;


/**
 * Класс, представляющий действие пользователя. (Регистрация, лог ин, лог аут)
 * наследует {@link Auditable}
 */
@Data
public class Action extends Auditable {
    /**
     * Тип действия из перечисления {@link ActionType}
     * (LOGIN, LOGOUT, REGISTRATION)
     */
    private ActionType type;

    /**
     * Конструктор для создания объекта действия (Action).
     *
     * @param id        Уникальный идентификатор действия
     * @param userId    Идентификатор пользователя, связанного с действием
     * @param dateTime  Дата и время создания действия
     * @param status    Статус действия, представленный в виде перечисления AuditableStatus
     * @param actionType Тип действия, представленный в виде перечисления ActionType
     */
    public Action(long id,
                  long userId,
                  LocalDateTime dateTime,
                  AuditableStatus status,
                  ActionType actionType) {

        super(id, userId, dateTime, status);
        this.type = actionType;
    }

    public Action(long userId,
                  LocalDateTime dateTime,
                  AuditableStatus status,
                  ActionType actionType) {

        super(userId, dateTime, status);
        this.type = actionType;
    }
}
