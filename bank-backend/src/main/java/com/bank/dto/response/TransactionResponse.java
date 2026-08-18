package com.bank.dto.response;

import com.bank.model.Transaction;

public class TransactionResponse {

    public int id;
    public Integer accountId;
    public String type;
    public Integer fromAccount;
    public Integer toAccount;
    public double amount;
    public String description;
    public String timestamp;


    public TransactionResponse(Transaction transaction) {

        this.id = transaction.getId();
        this.accountId = transaction.getAccountId();
        this.type = transaction.getType();
        this.fromAccount = transaction.getFromAccount();
        this.toAccount = transaction.getToAccount();
        this.amount = transaction.getAmount();
        this.description = transaction.getDescription();
        this.timestamp = transaction.getTimestamp();
    }
}

