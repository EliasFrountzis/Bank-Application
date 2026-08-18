package com.bank.model;

public class Transaction {

    private int id;
    private Integer accountId;
    private String type;
    private Integer fromAccount;
    private Integer toAccount;
    private double amount;
    private String description;
    private String timestamp;


    // DEPOSIT / WITHDRAWAL
    public Transaction(
            int id,
            int accountId,
            double amount,
            String type,
            String description
    ) {

        this.id = id;
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.description = description;

        this.timestamp =
                java.time.LocalDateTime.now().toString();
    }


    // TRANSFER
    public Transaction(
            int id,
            int fromAccount,
            int toAccount,
            double amount,
            String description
    ) {

        this.id = id;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.description = description;
        this.type = "TRANSFER";

        this.timestamp =
                java.time.LocalDateTime.now().toString();
    }


    // Used when reading transactions from PostgreSQL
    public Transaction(
            int id,
            Integer accountId,
            String type,
            Integer fromAccount,
            Integer toAccount,
            double amount,
            String description,
            String timestamp
    ) {

        this.id = id;
        this.accountId = accountId;
        this.type = type;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.description = description;
        this.timestamp = timestamp;
    }


    public int getId() {
        return id;
    }


    public Integer getAccountId() {
        return accountId;
    }


    public String getType() {
        return type;
    }


    public Integer getFromAccount() {
        return fromAccount;
    }


    public Integer getToAccount() {
        return toAccount;
    }


    public double getAmount() {
        return amount;
    }


    public String getDescription() {
        return description;
    }


    public String getTimestamp() {
        return timestamp;
    }
}

