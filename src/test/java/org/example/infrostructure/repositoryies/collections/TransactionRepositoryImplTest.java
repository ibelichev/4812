package org.example.infrostructure.repositoryies.collections;

import org.example.core.models.Transaction;
import org.example.core.models.enums.AuditableStatus;
import org.example.core.models.enums.TransactionType;
import org.example.core.repositories.TransactionRepository;
import org.example.infrostructure.repositoryies.collections.TransactionRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class TransactionRepositoryImplTest {

    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        transactionRepository = new TransactionRepositoryImpl();
    }

    @Test
    void addTransactionTest() {
        Transaction transaction = new Transaction(1, 2, LocalDateTime.now(),
                TransactionType.CREDIT, AuditableStatus.SUCCESS, 123);
        transactionRepository.addTransaction(transaction);
        List<Transaction> transactions = transactionRepository.getAll();
        assertThat(transactions).isNotEmpty();
        assertThat(transactions).contains(transaction);
    }

    @Test
    void findByIdTest() {
        long id = 1;
        Transaction transaction = new Transaction(id, 2, LocalDateTime.now(),
                TransactionType.CREDIT, AuditableStatus.SUCCESS, 321);
        Transaction transaction1 = new Transaction(3, 2, LocalDateTime.now(),
                TransactionType.CREDIT, AuditableStatus.SUCCESS, 123);
        Transaction transaction2 = new Transaction(2, id, LocalDateTime.now(),
                TransactionType.CREDIT, AuditableStatus.SUCCESS, 123);
        transactionRepository.addTransaction(transaction);
        transactionRepository.addTransaction(transaction1);
        transactionRepository.addTransaction(transaction2);
        Transaction found = transactionRepository.findById(id);
        Transaction found1 = transactionRepository.findById(1232);
        assertThat(found).isEqualTo(transaction);
        assertThat(found1).isNull();
    }

    @Test
    void findByIdNullTest() {
        long id = 1;
        Transaction transaction = new Transaction(id, 2, LocalDateTime.now(),
                TransactionType.CREDIT, AuditableStatus.SUCCESS, 321);
        Transaction transaction1 = new Transaction(3, 2, LocalDateTime.now(),
                TransactionType.CREDIT, AuditableStatus.SUCCESS, 123);
        Transaction transaction2 = new Transaction(2, id, LocalDateTime.now(),
                TransactionType.CREDIT, AuditableStatus.SUCCESS, 123);
        transactionRepository.addTransaction(transaction);
        transactionRepository.addTransaction(transaction1);
        transactionRepository.addTransaction(transaction2);
        Transaction found = transactionRepository.findById(234);
        assertThat(found).isNull();
    }


    @Test
    void findAllByUserIdTest() {
        long userId = 1;
        Transaction transaction = new Transaction(1, userId, LocalDateTime.now(),
                TransactionType.CREDIT, AuditableStatus.SUCCESS, 321);
        Transaction transaction1 = new Transaction(3, userId, LocalDateTime.now(),
                TransactionType.CREDIT, AuditableStatus.SUCCESS, 123);
        Transaction transaction2 = new Transaction(2, 2, LocalDateTime.now(),
                TransactionType.CREDIT, AuditableStatus.SUCCESS, 123);
        transactionRepository.addTransaction(transaction);
        transactionRepository.addTransaction(transaction1);
        transactionRepository.addTransaction(transaction2);
        List<Transaction> found = transactionRepository.findAllByUserId(userId);
        List<Transaction> expected = new ArrayList<>();
        expected.add(transaction);
        expected.add(transaction1);
        assertThat(found).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void isTransactionIdUnique() {
        boolean test1 = transactionRepository.isTransactionIdUnique(1);
        boolean test2 = transactionRepository.isTransactionIdUnique(1);
        boolean test3 = transactionRepository.isTransactionIdUnique(2);
        assertThat(test1).isTrue();
        assertThat(test2).isFalse();
        assertThat(test3).isTrue();
    }
}