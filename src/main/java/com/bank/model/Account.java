package com.bank.model;

public class Account {

    private int id;
    private int userId;
    private double balance;
    private String cardLast4;

    public Account(int id, int userId, double balance, String cardLast4) {
        this.id = id;
        this.userId = userId;
        this.balance = balance;
        this.cardLast4 = cardLast4;
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

    public void deposit(double amount) {
        balance += amount;
    }

    public void withdraw(double amount) {
        balance -= amount;
    }
}