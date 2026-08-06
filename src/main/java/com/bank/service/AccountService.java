package com.bank.service;

import java.util.List;

import com.bank.exception.BankException;
import com.bank.model.Account;
import com.bank.repository.AccountRepository;


public class AccountService {

    private final AccountRepository accountRepository;
    private int nextId = 1;


    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }


    public Account createAccount(String owner, double initialBalance){

      if (owner == null || owner.isBlank()) {

    throw new BankException(
            "Owner cannot be empty",
            400
    );

}


if (initialBalance < 0) {

    throw new BankException(
            "Initial balance cannot be negative",
            400
    );

}

        Account account =
            new Account(nextId++, owner, initialBalance);


        return accountRepository.save(account);
    }


    public List<Account> getAccounts(){

        return accountRepository.findAll();

    }


   public Account getAccountById(int id) {

    Account account = accountRepository.findById(id);


    if(account == null){

        throw new BankException(
                "Account with id " + id + " not found",
                404
        );

    }


    return account;

}

}