package com.bank.service;

import com.bank.exception.BankException;
import com.bank.model.Account;

public class TransferService {

    private final AccountService accountService;
    private final TransactionService transactionService;


    public TransferService(
            AccountService accountService,
            TransactionService transactionService
    ) {

        this.accountService = accountService;
        this.transactionService = transactionService;

    }


    public void transfer(int fromId, int toId, double amount) {

        Account sender = accountService.getAccountById(fromId);

        Account receiver = accountService.getAccountById(toId);

if(fromId == toId){

        throw new BankException(
                "Cannot transfer to the same account",
                400
        );

    }
    

       if(amount <= 0){

    throw new BankException(
            "Transfer amount must be positive",
            400
    );

}


       if(sender.getBalance() < amount){

    throw new BankException(
            "Insufficient funds",
            400
    );

}

        sender.withdraw(amount);

        receiver.deposit(amount);


        transactionService.createTransaction(
                fromId,
                toId,
                amount
        );

    }

}