package com.bank.service;

import com.bank.exception.BankException;
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


    public List<Transaction> getTransactionsByAccount(int accountId){

        return transactionRepository.findByAccountId(accountId);

    }

}