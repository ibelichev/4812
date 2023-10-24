package org.example.infrostructure.services;

import org.example.core.models.Auditable;
import org.example.core.models.Transaction;
import org.example.core.models.User;
import org.example.core.models.enums.AuditableStatus;
import org.example.core.models.enums.TransacionReturns;
import org.example.core.models.enums.TransactionType;
import org.example.core.repositories.AuditableRepository;
import org.example.core.repositories.TransactionRepository;
import org.example.core.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OperationServiceImplTest {

    private OperationServiceImpl operationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AuditableRepository auditableRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        operationService = new OperationServiceImpl(userRepository, transactionRepository, auditableRepository);
    }

    @Test
    void testCreditSuccess() {
        User user = new User("username", "password", "f1", "l1", 100);
        float initialBalance = user.getBalance();
        float amount = 50;
        long transactionId = 1;
        when(transactionRepository.isTransactionIdUnique(transactionId)).thenReturn(true);

        TransacionReturns result = operationService.credit(user, amount);

        assertEquals(TransacionReturns.SUCCESS, result);
        verify(userRepository).updateUser(user);
        verify(transactionRepository).addTransaction(any(Transaction.class));
        verify(auditableRepository).addAuditable(any(Transaction.class));
        assertEquals(initialBalance + amount, user.getBalance(), 0.01f);
    }

    @Test
    void testDebitSuccess() {
        User user = new User("username", "password", "f3", "l3", 300);
        float initialBalance = user.getBalance();
        float amount = 100;
        long transactionId = 3;
        when(transactionRepository.isTransactionIdUnique(transactionId)).thenReturn(true);

        TransacionReturns result = operationService.debit(user, amount);

        assertEquals(TransacionReturns.SUCCESS, result);
        verify(userRepository).updateUser(user);
        verify(transactionRepository).addTransaction(any(Transaction.class));
        verify(auditableRepository).addAuditable(any(Transaction.class));
        assertEquals(initialBalance - amount, user.getBalance(), 0.01f);
    }

    @Test
    void testHistory() {
        User user = new User("username", "password", "f5", "l5", 500);
        when(transactionRepository.findAllByUserId(user.getId())).thenReturn(List.of(
                new Transaction(1, user.getId(), LocalDateTime.now(), TransactionType.CREDIT, AuditableStatus.SUCCESS, 100),
                new Transaction(2, user.getId(), LocalDateTime.now(), TransactionType.DEBIT, AuditableStatus.SUCCESS, 50)
        ));

        List<Transaction> result = operationService.history(user);

        assertEquals(2, result.size());
        verify(transactionRepository).findAllByUserId(user.getId());
    }

    @Test
    void testAudit() {
        User user = new User("username", "password", "f6", "l6", 600);
        when(auditableRepository.findAllByUserId(user.getId())).thenReturn(List.of(
                new Transaction(1, user.getId(), LocalDateTime.now(), TransactionType.CREDIT, AuditableStatus.SUCCESS, 100),
                new Transaction(2, user.getId(), LocalDateTime.now(), TransactionType.DEBIT, AuditableStatus.SUCCESS, 50)
        ));

        List<Auditable> result = operationService.audit(user);

        assertEquals(2, result.size());
        verify(auditableRepository).findAllByUserId(user.getId());
    }

    @Test
    void testDebitNotEnoughMoney() {
        User user = new User("username", "password", "f6", "l6", 600);
        float amount = 700;
        long transactionId = 6;
        when(transactionRepository.isTransactionIdUnique(transactionId)).thenReturn(true);

        TransacionReturns result = operationService.debit(user, amount);

        assertEquals(TransacionReturns.NOT_ENOUGH_MONEY, result);
        verify(userRepository, Mockito.never()).updateUser(user);
        verify(transactionRepository).addTransaction(any(Transaction.class));
        verify(auditableRepository).addAuditable(any(Transaction.class));
    }
}
