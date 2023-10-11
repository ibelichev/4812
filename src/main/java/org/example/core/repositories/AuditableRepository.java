package org.example.core.repositories;

import org.example.core.models.Action;
import org.example.core.models.Auditable;
import org.example.core.models.Transaction;

import java.util.List;

/**
 * Интерфейс, представляющий репозиторий для сущностей,
 * наследующих {@link Auditable},
 * это {@link Transaction} и {@link Action}
 */
public interface AuditableRepository {

    /**
     * Добавляет действие в репозиторий.
     *
     * @param auditable действие для добавления.
     */

    void addAuditable(Auditable auditable);

    /**
     * Находит аудит по его уникальному id
     *
     * @param id id аудита
     * @return Аудит, если найден, или null, если не найден
     */
    Auditable findById(long id);

    /**
     * Возвращает список аудита, связанного с конкретным пользователем
     *
     * @param userId id пользователя, чей аудит требуется найти
     * @return Список аудита, связанного с указанным пользователем
     */
    List<Auditable> findAllByUserId(long userId);

    /**
     * Возвращает все записи из репозитория
     *
     * @return Список всех аудиторских записей
     */
    List<Auditable> getAll();
}

