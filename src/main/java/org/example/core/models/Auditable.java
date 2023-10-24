package org.example.core.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.core.models.enums.AuditableStatus;

import java.time.LocalDateTime;

/**
 * Клас описывающий общие параметры для действий и транзакций
 * {@link Action} и {@link Transaction}
 */
@NoArgsConstructor
@Data
public abstract class Auditable {
    /**
     * Уникальный идентификатор для элемента
     */
    private long id;

    /**
     * Идентификатор пользователя, связанного с элементом
     */
    private long userId;

    /**
     * Дата и время, когда элемент был создан
     */
    private LocalDateTime dateTime;

    /**
     * Статус элемента, представленный в виде перечисления AuditableStatus
     */
    private AuditableStatus status;

    /**
     * Конструктор класса, принимающий параметры
     *
     * @param id     Уникальный идентификатор элемента
     * @param userId Идентификатор пользователя, связанного с элементом
     * @param dateTime Дата и время создания элемента
     * @param status Статус элемента, представленный в виде перечисления AuditableStatus
     */
    public Auditable(long id, long userId, LocalDateTime dateTime, AuditableStatus status) {
        this.id = id;
        this.userId = userId;
        this.dateTime = dateTime;
        this.status = status;
    }

    public Auditable(long userId, LocalDateTime dateTime, AuditableStatus status) {
        this.userId = userId;
        this.dateTime = dateTime;
        this.status = status;
    }
}
