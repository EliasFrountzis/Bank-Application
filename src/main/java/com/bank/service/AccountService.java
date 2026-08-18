package com.bank.service;

import java.util.List;

import com.bank.exception.BankException;
import com.bank.model.Account;
import com.bank.model.Transaction;
import com.bank.repository.AccountRepository;
import com.bank.repository.TransactionRepository;

public class AccountService {

    private final AccountRepository accountRepository;
    private final UserService userService;
    private final TransactionRepository transactionRepository;


    public AccountService(
            AccountRepository accountRepository,
            UserService userService,
            TransactionRepository transactionRepository
    ) {
        this.accountRepository = accountRepository;
        this.userService = userService;
        this.transactionRepository = transactionRepository;
    }


    public Account createAccount(
            int userId,
            double initialBalance,
            String cardLast4,
            String name,
            String type
    ) {

        userService.getUserById(userId);


        if (initialBalance < 0) {

            throw new BankException(
                    "Initial balance cannot be negative",
                    400
            );
        }


        if (name == null || name.isBlank()) {

            throw new BankException(
                    "Account name cannot be empty",
                    400
            );
        }


        if (
                !type.equals("CURRENT") &&
                !type.equals("SAVINGS")
        ) {

            throw new BankException(
                    "Account type must be CURRENT or SAVINGS",
                    400
            );
        }


        Account account =
                new Account(
                        0,
                        userId,
                        initialBalance,
                        cardLast4,
                        name,
                        type,
                        "ACTIVE"
                );


        return accountRepository.save(account);
    }


    public List<Account> getAccounts() {

        return accountRepository.findAll();
    }


    public Account getAccountById(int id) {

        Account account =
                accountRepository.findById(id);


        if (account == null) {

            throw new BankException(
                    "Account with id " + id + " not found",
                    404
            );
        }


        return account;
    }


    public void updateAccount(Account account) {

        accountRepository.update(account);
    }

    public Account closeAccount(int accountId) {

    Account account =
            getAccountById(accountId);

    if (account.getStatus().equals("CLOSED")) {

        throw new BankException(
                "Account is already closed",
                400
        );
    }

    account.close();

    accountRepository.update(account);

    return account;
}


    public Account deposit(
            int accountId,
            double amount
    ) {

        if (amount <= 0) {

            throw new BankException(
                    "Deposit amount must be positive",
                    400
            );
        }


        Account account =
                getAccountById(accountId);


        if (account.getStatus().equals("CLOSED")) {

            throw new BankException(
                    "Cannot deposit into a closed account",
                    400
            );
        }


        account.deposit(amount);

        accountRepository.update(account);


        Transaction transaction =
                new Transaction(
                        0,
                        accountId,
                        amount,
                        "DEPOSIT",
                        "Cash deposit"
                );


        transactionRepository.save(transaction);


        return account;
    }


    public Account withdraw(
            int accountId,
            double amount
    ) {

        if (amount <= 0) {

            throw new BankException(
                    "Withdrawal amount must be positive",
                    400
            );
        }


        Account account =
                getAccountById(accountId);


        if (account.getStatus().equals("CLOSED")) {

            throw new BankException(
                    "Cannot withdraw from a closed account",
                    400
            );
        }


        if (account.getBalance() < amount) {

            throw new BankException(
                    "Insufficient funds",
                    400
            );
        }


        account.withdraw(amount);

        accountRepository.update(account);


        Transaction transaction =
                new Transaction(
                        0,
                        accountId,
                        amount,
                        "WITHDRAWAL",
                        "Cash withdrawal"
                );


        transactionRepository.save(transaction);


        return account;
    }


    public List<Account> getAccountsByUserId(
            int userId
    ) {

        userService.getUserById(userId);

        return accountRepository.findByUserId(userId);
    }
}