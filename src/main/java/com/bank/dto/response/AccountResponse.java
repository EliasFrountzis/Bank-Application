package com.bank.dto.response;

import com.bank.model.Account;

public class AccountResponse {

    public int id;
    public String owner;
    public double balance;

    public AccountResponse(Account account) {
        this.id = account.getId();
        this.owner = account.getOwner();
        this.balance = account.getBalance();
    }
}