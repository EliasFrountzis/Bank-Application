package com.bank.model;

public class Account {

    private int id;
    private int userId;
    private double balance;
    private String cardLast4;

    private String name;
    private String type;
    private String status;


    public Account(
            int id,
            int userId,
            double balance,
            String cardLast4,
            String name,
            String type,
            String status
    ) {

        this.id = id;
        this.userId = userId;
        this.balance = balance;
        this.cardLast4 = cardLast4;

        this.name = name;
        this.type = type;
        this.status = status;
    }


    public int getId() {
        return id;
    }


    public int getUserId() {
        return userId;
    }


    public double getBalance() {
        return balance;
    }


    public String getCardLast4() {
        return cardLast4;
    }


    public String getName() {
        return name;
    }


    public String getType() {
        return type;
    }


    public String getStatus() {
        return status;
    }


    public void deposit(double amount) {
        balance += amount;
    }


    public void withdraw(double amount) {
        balance -= amount;
    }


    public void updateDetails(
            String name,
            String type
    ) {

        this.name = name;
        this.type = type;
    }


    public void close() {
        this.status = "CLOSED";
    }
}