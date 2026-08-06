package com.bank.repository;


import java.util.ArrayList;
import java.util.List;

import com.bank.model.Transaction;


public class InMemoryTransactionRepository 
        implements TransactionRepository {


    private final List<Transaction> transactions =
            new ArrayList<>();


    @Override
    public Transaction save(Transaction transaction){

        transactions.add(transaction);

        return transaction;
    }


    @Override
    public List<Transaction> findAll(){

        return new ArrayList<>(transactions);

    }

}