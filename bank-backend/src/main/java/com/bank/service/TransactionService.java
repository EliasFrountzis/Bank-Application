package com.bank.service;

import com.bank.exception.BankException;
import com.bank.model.Transaction;
import com.bank.repository.TransactionRepository;

import java.util.List;

public class TransactionService {

    private final TransactionRepository transactionRepository;


    public TransactionService(
            TransactionRepository transactionRepository
    ) {
        this.transactionRepository = transactionRepository;
    }


    public Transaction createTransaction(
            int fromId,
            int toId,
            double amount,
            String description
    ) {

        if (fromId == toId) {

            throw new BankException(
                    "Cannot transfer money to the same account",
                    400
            );
        }


        if (amount <= 0) {

            throw new BankException(
                    "Transaction amount must be positive",
                    400
            );
        }


        if (description == null || description.isBlank()) {

            throw new BankException(
                    "Transaction description cannot be empty",
                    400
            );
        }


        Transaction transaction =
                new Transaction(
                        0,
                        fromId,
                        toId,
                        amount,
                        description
                );


        return transactionRepository.save(transaction);
    }


    public List<Transaction> getTransactions() {

        return transactionRepository.findAll();
    }


    public List<Transaction> getTransactionsByAccount(
            int accountId
    ) {

        return transactionRepository.findByAccountId(accountId);
    }
}

