package org.example.core.models.enums;

/**
 * Перечисления статуса операции для наследников абстрактного класса
 * {@link org.example.core.models.Auditable}
 * которыми являются:
 * {@link org.example.core.models.Action}
 * и {@link org.example.core.models.Transaction}
 */
public enum AuditableStatus {
    SUCCESS,
    DECLINE
}
