package com.bank;

import static spark.Spark.*;

import com.bank.controller.AccountController;
import com.bank.controller.TransferController;

import com.bank.service.AccountService;
import com.bank.service.TransferService;
import com.bank.service.TransactionService;
import com.bank.controller.TransactionController;


public class BankApplication {


    private static final AccountService accountService =
            new AccountService();


    private static final TransactionService transactionService =
            new TransactionService();


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