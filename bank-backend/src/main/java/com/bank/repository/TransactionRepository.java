package com.bank.repository;

import java.util.List;

import com.bank.model.Transaction;


public interface TransactionRepository {


    Transaction save(Transaction transaction);


    List<Transaction> findAll();
    List<Transaction> findByAccountId(int accountId);

}