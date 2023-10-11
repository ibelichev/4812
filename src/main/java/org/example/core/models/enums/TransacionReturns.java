package org.example.core.models.enums;

import org.example.core.models.User;

/**
 * Перечисление возвращаемых данных
 * {@link org.example.core.services.OperationService#credit(User, float, long)}
 * и {@link org.example.core.services.OperationService#debit(User, float, long)}
 */
public enum TransacionReturns {
    SUCCESS,
    UNUNIQUE_ID,
    NOT_ENOUGH_MONEY
}
