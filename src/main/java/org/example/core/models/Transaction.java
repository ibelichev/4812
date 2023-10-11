package org.example.core.models;

import lombok.Data;
import org.example.core.models.enums.AuditableStatus;
import org.example.core.models.enums.TransactionType;

import java.time.LocalDateTime;

/**
 * Класс, представляющий транзакцию
 * Транзакции представляют собой действия, связанные с изменением баланса пользователя
 * (debit и credit)
 */
@Data
public class Transaction extends Auditable {
    /**
     * Тип транзакции, определенный перечислением {@link TransactionType}
     */
    private TransactionType type;

    /**
     * Сумма транзакции, указанная в денежных единицах
     */
    private float amount;

    /**
     * Конструктор для создания объекта транзакции
     *
     * @param id        Уникальный идентификатор транзакции
     * @param userId    Идентификатор пользователя, связанного с транзакцией
     * @param dateTime  Дата и время создания транзакции
     * @param type      Тип транзакции, представленный в виде перечисления {@link TransactionType}
     * @param status    Статус транзакции, представленный в виде перечисления {@link AuditableStatus}
     * @param amount    Сумма транзакции
     */
    public Transaction(long id, long userId, LocalDateTime dateTime, TransactionType type, AuditableStatus status, float amount) {
        super(id, userId, dateTime, status);
        this.type = type;
        this.amount = amount;
    }
}

