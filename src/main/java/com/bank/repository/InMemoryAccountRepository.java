package com.bank.repository;

import java.util.ArrayList;
import java.util.List;

import com.bank.model.Account;


public class InMemoryAccountRepository implements AccountRepository {


    private final List<Account> accounts = new ArrayList<>();


    @Override
    public Account save(Account account) {

        accounts.add(account);

        return account;
    }


    @Override
    public List<Account> findAll() {

        return new ArrayList<>(accounts);

    }


    @Override
    public Account findById(int id) {

        for(Account account : accounts){

            if(account.getId() == id){
                return account;
            }

        }

        return null;
    }

}