package org.example.core.services;
import org.example.core.models.Auditable;
import org.example.core.models.Transaction;
import org.example.core.models.User;
import org.example.core.models.enums.TransacionReturns;

import java.util.List;

/**
 * Интерфейс сервиса для выполнения пользовательских действий:
 * кредит, дебет, история и аудит
 */
public interface OperationService {

    /**
     * Осуществляет операцию пополнения счета пользователя
     *
     * @param user   Пользователь
     * @param amount Сумма пополнения
     * @param id     Уникальный идентификатор транзакции
     * @return Статус выполнения операции
     */
    TransacionReturns credit(User user, float amount, long id);

    /**
     * Осуществляет операцию снятия средств со счета пользователя
     *
     * @param user   Пользователь
     * @param amount Сумма снятия
     * @param id     Уникальный идентификатор транзакции
     * @return Статус выполнения операции
     */
    TransacionReturns debit(User user, float amount, long id);

    /**
     * Получает историю <b>транзакций</b> для конкретного пользователя
     *
     * @param user Пользователь
     * @return Список транзакций пользователя
     */
    List<Transaction> history(User user);

    /**
     * Получает аудит действий пользователя (транзакции, логин, логаут, регистрация)
     *
     * @param user Пользователь
     * @return Список действий для пользователя
     */
    List<Auditable> audit(User user);
}
