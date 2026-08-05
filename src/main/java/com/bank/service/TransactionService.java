package com.bank.service;

import com.bank.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionService {

    private final List<Transaction> transactions = new ArrayList<>();

    private int nextId = 1;


    public Transaction createTransaction(
            int fromAccount,
            int toAccount,
            double amount
    ) {

        Transaction transaction =
                new Transaction(
                        nextId++,
                        fromAccount,
                        toAccount,
                        amount
                );

        transactions.add(transaction);

        return transaction;
    }


    public List<Transaction> getTransactions() {

        return new ArrayList<>(transactions);

    }

}