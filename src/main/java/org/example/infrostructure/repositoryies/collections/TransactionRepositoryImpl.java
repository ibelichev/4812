package org.example.infrostructure.repositoryies.collections;

import lombok.RequiredArgsConstructor;
import org.example.core.models.Transaction;
import org.example.core.repositories.TransactionRepository;

import java.util.*;

/**
 * Реализация интерфейса {@link TransactionRepository},
 * предоставляющая функциональность для работы с транзакциями в системе
 */
@RequiredArgsConstructor
public class TransactionRepositoryImpl implements TransactionRepository {
    private final List<Transaction> transactions = new ArrayList<>();
    private final Set<Long> transactionIds = new HashSet<>();

    @Override
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    @Override
    public Transaction findById(long id) {
        Optional<Transaction> transaction = transactions.stream().filter(t -> t.getId() == id).findFirst();
        return transaction.orElse(null);
    }

    @Override
    public List<Transaction> findAllByUserId(long userId) {
        List<Transaction> userTransactions = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (transaction.getUserId() == userId) {
                userTransactions.add(transaction);
            }
        }

        return userTransactions;
    }

    @Override
    public List<Transaction> getAll() {
        return transactions;
    }

    @Override
    public boolean isTransactionIdUnique(long transactionId) {
        if (transactionIds.contains(transactionId)) {
            return false;
        }
        transactionIds.add(transactionId);
        return true;
    }
}
