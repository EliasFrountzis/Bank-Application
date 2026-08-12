package com.bank.dto.response;

import com.bank.model.Account;

public class AccountResponse {

public int id;
public int userId;
public double balance;
public String cardLast4;

public AccountResponse(Account account) {
    this.id = account.getId();
    this.userId = account.getUserId();
    this.balance = account.getBalance();
    this.cardLast4 = account.getCardLast4();
}


}
