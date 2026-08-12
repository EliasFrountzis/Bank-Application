package com.bank.service;

import com.bank.exception.BankException;
import com.bank.model.Account;
import com.bank.repository.TransferRepository;

public class TransferService {

    private final AccountService accountService;
    private final TransferRepository transferRepository;

    public TransferService(
            AccountService accountService,
            TransferRepository transferRepository
    ) {
        this.accountService = accountService;
        this.transferRepository = transferRepository;
    }

    public void transfer(
            int fromId,
            int toId,
            double amount,
            String description
    ) {

        Account sender =
                accountService.getAccountById(fromId);

        accountService.getAccountById(toId);

        if (fromId == toId) {

            throw new BankException(
                    "Cannot transfer to the same account",
                    400
            );

        }

        if (amount <= 0) {

            throw new BankException(
                    "Transfer amount must be positive",
                    400
            );

        }

        if (sender.getBalance() < amount) {

            throw new BankException(
                    "Insufficient funds",
                    400
            );

        }

        transferRepository.transfer(
                fromId,
                toId,
                amount,
                description
        );
    }
}