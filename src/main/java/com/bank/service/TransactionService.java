package com.bank.service;

import com.bank.model.Transaction;
import com.bank.repository.TransactionRepository;

import java.util.List;

public class TransactionService {


    private final TransactionRepository transactionRepository;

    private int nextId = 1;
    public TransactionService(
            TransactionRepository transactionRepository
    ){
        this.transactionRepository = transactionRepository;
    }


    public Transaction createTransaction(
            int fromId,
            int toId,
            double amount
    ){

        Transaction transaction =
        new Transaction(
                nextId++,
                fromId,
                toId,
                amount
        );


        return transactionRepository.save(transaction);
    }


    public List<Transaction> getTransactions(){

        return transactionRepository.findAll();

    }

}