package com.bank;

import java.util.ArrayList;
import java.util.List;

public class AccountService {

    private final List<Account> accounts = new ArrayList<>();
    private int nextId = 1;

    public Account createAccount(String owner, double initialBalance) {

    if (owner == null || owner.isBlank()) {
        throw new IllegalArgumentException("Owner cannot be empty.");
    }

    if (initialBalance < 0) {
        throw new IllegalArgumentException("Initial balance cannot be negative.");
    }

    Account account = new Account(nextId++, owner, initialBalance);

    accounts.add(account);

    return account;
}

    public List<Account> getAccounts() {
        return accounts;
    }

    public Account getAccountById(int id) {

    for (int i = 0; i < accounts.size(); i++) {

        Account account = accounts.get(i);

        if (account.getId() == id) {
            return account;
        }

    }

    return null;
}

}