package org.example.core.repositories;

import org.example.core.models.Transaction;

import java.util.List;

/**
 * Интерфейс, представляющий репозиторий для транзакций
 * {@link Transaction}
 */
public interface TransactionRepository {

    /**
     * Добавляет транзакцию в репозиторий
     *
     * @param transaction Транзакция для добавления
     */
    void addTransaction(Transaction transaction);

    /**
     * Находит транзакцию по ее уникальному идентификатору
     *
     * @param id id транзакции
     * @return Транзакция, если найдена, или null, если не найдена
     */
    Transaction findById(long id);

    /**
     * Возвращает список транзакций, связанных с конкретным пользователем
     *
     * @param userId id пользователя, чьи транзакции требуется найти
     * @return Список транзакций, связанных с указанным пользователем
     */
    List<Transaction> findAllByUserId(long userId);

    /**
     * Проверяет, уникален ли id транзакции
     *
     * @param transactionId id транзакции для проверки уникальности
     * @return true, если id уникален, иначе false
     */
    boolean isTransactionIdUnique(long transactionId);

    /**
     * Возвращает все транзакции из репозитория
     *
     * @return Список всех транзакций
     */
    List<Transaction> getAll();
}
