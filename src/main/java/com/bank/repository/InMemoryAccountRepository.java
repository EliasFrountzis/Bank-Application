package com.bank.repository;

import java.util.ArrayList;
import java.util.List;

import com.bank.model.Account;

public class InMemoryAccountRepository implements AccountRepository {

    private final List<Account> accounts =
            new ArrayList<>();

    private int nextId = 1;

    @Override
    public Account save(Account account) {

        Account saved =
                new Account(
                        nextId++,
                        account.getUserId(),
                        account.getBalance(),
                        account.getCardLast4()
                );

        accounts.add(saved);

        return saved;
    }

    @Override
    public List<Account> findAll() {
        return accounts;
    }

    @Override
    public Account findById(int id) {

        return accounts.stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void update(Account account) {
        // not needed for tests yet
    }
}