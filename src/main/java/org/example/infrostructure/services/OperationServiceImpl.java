package org.example.infrostructure.services;

import lombok.RequiredArgsConstructor;
import org.example.core.models.Auditable;
import org.example.core.models.Transaction;
import org.example.core.models.User;
import org.example.core.models.enums.AuditableStatus;
import org.example.core.models.enums.TransacionReturns;
import org.example.core.models.enums.TransactionType;
import org.example.core.repositories.AuditableRepository;
import org.example.core.repositories.TransactionRepository;
import org.example.core.repositories.UserRepository;
import org.example.core.services.OperationService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Реализация {@link OperationService}
 */
@RequiredArgsConstructor
public class OperationServiceImpl implements OperationService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final AuditableRepository auditableRepository;

    @Override
    public TransacionReturns credit(User user, float amount) {
        long userId = user.getId();
        float balance = user.getBalance();
        LocalDateTime dateTime = LocalDateTime.now();
        AuditableStatus status;

        status = AuditableStatus.SUCCESS;
        user.setBalance(balance + amount);
        userRepository.updateUser(user);
        Transaction transaction = new Transaction(userId, dateTime,
                TransactionType.CREDIT, status, amount);
        transactionRepository.addTransaction(transaction);
        auditableRepository.addAuditable(transaction);
        return TransacionReturns.SUCCESS;
    }


    @Override
    public TransacionReturns debit(User user, float amount) {
        long userId = user.getId();
        float balance = user.getBalance();
        LocalDateTime dateTime = LocalDateTime.now();
        AuditableStatus status;

        if (balance >= amount) {

            // сохранение транзакции как успешной, изменение баланс, если уникален id
            // и достаточно средств
            user.setBalance(balance - amount);
            userRepository.updateUser(user);
            status = AuditableStatus.SUCCESS;
            Transaction transaction = new Transaction(userId, dateTime,
                    TransactionType.DEBIT, status, amount);
            transactionRepository.addTransaction(transaction);
            auditableRepository.addAuditable(transaction);

            // возвращает статус операции
            return TransacionReturns.SUCCESS;

        } else {
            // сохранение транзакции как успешной, баланс не меняется т.к. недостаточно средств
            status = AuditableStatus.DECLINE;
            Transaction transaction = new Transaction(userId, dateTime,
                    TransactionType.DEBIT, status, amount);
            transactionRepository.addTransaction(transaction);
            auditableRepository.addAuditable(transaction);

            // возвращает статус операции
            return TransacionReturns.NOT_ENOUGH_MONEY;
        }
    }

    @Override
    public List<Transaction> history(User user) {
        return transactionRepository.findAllByUserId(user.getId());
    }

    @Override
    public List<Auditable> audit(User user) {
        return auditableRepository.findAllByUserId(user.getId());
    }
}
