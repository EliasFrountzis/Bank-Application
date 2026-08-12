package com.bank.repository;

import java.util.HashMap;
import java.util.Map;

import com.bank.model.Account;

public class InMemoryTransferRepository 
        implements TransferRepository {


    private final Map<Integer, Account> accounts =
            new HashMap<>();


    public void addAccount(Account account) {

        accounts.put(
                account.getId(),
                account
        );

    }



    @Override
        public void transfer(
                int fromAccount,
                int toAccount,
                double amount,
                String description
        ){


        Account sender =
                accounts.get(fromAccount);


        Account receiver =
                accounts.get(toAccount);



        sender.withdraw(amount);

        receiver.deposit(amount);

    }

}