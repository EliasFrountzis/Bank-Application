package com.bank;

import static spark.Spark.*;

import com.bank.controller.AccountController;
import com.bank.controller.TransferController;

import com.bank.service.AccountService;
import com.bank.repository.AccountRepository;
import com.bank.repository.PostgresAccountRepository;
import com.bank.repository.PostgresTransactionRepository;
import com.bank.repository.TransactionRepository;
import com.bank.service.TransferService;
import com.bank.service.TransactionService;
import com.bank.controller.TransactionController;
import com.bank.exception.ExceptionHandler;


public class BankApplication {


  private static final AccountRepository accountRepository =
        new PostgresAccountRepository();


private static final AccountService accountService =
        new AccountService(accountRepository);



private static final TransactionRepository transactionRepository =
        new PostgresTransactionRepository();


private static final TransactionService transactionService =
        new TransactionService(transactionRepository);



private static final TransferService transferService =
        new TransferService(
                accountService,
                transactionService
        );


    private static final TransferController transferController =
            new TransferController(transferService);


            private static final TransactionController transactionController =
        new TransactionController(transactionService);



    public static void main(String[] args) {

        System.out.println("Starting bank backend...");

        port(4567);



        ExceptionHandler.register();

        AccountController accountController =
                new AccountController(accountService);


        accountController.registerRoutes();

        transferController.registerRoutes();

        transactionController.registerRoutes();


        System.out.println("Server configured");


        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }

}