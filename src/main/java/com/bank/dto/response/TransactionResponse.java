package com.bank.dto.response;

import com.bank.model.Transaction;

public class TransactionResponse {

    public int id;
    public int fromAccount;
    public int toAccount;
    public double amount;


    public TransactionResponse(Transaction transaction) {

        this.id = transaction.getId();
        this.fromAccount = transaction.getFromAccount();
        this.toAccount = transaction.getToAccount();
        this.amount = transaction.getAmount();

    }

}