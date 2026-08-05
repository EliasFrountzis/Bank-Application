package com.bank.service;

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


        if (sender == null || receiver == null) {
            throw new IllegalArgumentException("Account not found");
        }


        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }


        if (sender.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient funds");
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