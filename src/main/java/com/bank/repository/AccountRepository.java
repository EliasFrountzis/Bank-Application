package com.bank.repository;

import java.util.List;
import com.bank.model.Account;

public interface AccountRepository {

    Account save(Account account);

    List<Account> findAll();

    Account findById(int id);

    List<Account> findByUserId(int userId);

    void update(Account account);
}