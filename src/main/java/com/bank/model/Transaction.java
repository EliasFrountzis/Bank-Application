package com.bank.model;

public class Transaction {

    private int id;
    private int fromAccount;
    private int toAccount;
    private double amount;
    private String timestamp;


    public Transaction(
            int id,
            int fromAccount,
            int toAccount,
            double amount
    ) {

        this.id = id;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.timestamp =
                java.time.LocalDateTime.now().toString();
    }


    public Transaction(
            int id,
            int fromAccount,
            int toAccount,
            double amount,
            String timestamp
    ) {

        this.id = id;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.timestamp = timestamp;
    }


    public int getId() {
        return id;
    }


    public int getFromAccount() {
        return fromAccount;
    }


    public int getToAccount() {
        return toAccount;
    }


    public double getAmount() {
        return amount;
    }


    public String getTimestamp() {
        return timestamp;
    }
}

