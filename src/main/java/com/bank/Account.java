package com.bank;

public class Account {

    private int id;
    private String owner;
    private double balance;

    public Account(int id, String owner, double balance) {
        this.id = id;
        this.owner = owner;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public double getBalance() {
        return balance;
    }





    public void deposit(double amount) {
    balance += amount;
}

public void withdraw(double amount) {
    balance -= amount;
}





}