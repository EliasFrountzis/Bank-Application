package com.bank;

import static spark.Spark.*;

import com.bank.controller.AccountController;
import com.bank.controller.TransactionController;
import com.bank.controller.TransferController;
import com.bank.controller.UserController;
import com.bank.exception.ExceptionHandler;
import com.bank.repository.AccountRepository;
import com.bank.repository.PostgresAccountRepository;
import com.bank.repository.PostgresTransactionRepository;
import com.bank.repository.PostgresTransferRepository;
import com.bank.repository.PostgresUserRepository;
import com.bank.repository.TransactionRepository;
import com.bank.repository.TransferRepository;
import com.bank.repository.UserRepository;
import com.bank.service.AccountService;
import com.bank.service.TransactionService;
import com.bank.service.TransferService;
import com.bank.service.UserService;
import com.google.gson.Gson;

public class BankApplication {

    private static final Gson gson = new Gson();

    
    private static final UserRepository userRepository =
            new PostgresUserRepository();

    private static final UserService userService =
            new UserService(userRepository);

            
    private static final TransactionRepository transactionRepository =
            new PostgresTransactionRepository();


    
    private static final AccountRepository accountRepository =
            new PostgresAccountRepository();

    private static final AccountService accountService =
           new AccountService(
        accountRepository,
        userService,
        transactionRepository
);
    
    private static final TransactionService transactionService =
            new TransactionService(
                    transactionRepository
            );

    private static final TransferRepository transferRepository =
            new PostgresTransferRepository();

    private static final TransferService transferService =
            new TransferService(
                    accountService,
                    transferRepository
            );

   
    private static final UserController userController =
            new UserController(
                    userService,
                    gson
            );

    private static final AccountController accountController =
            new AccountController(
                    accountService
            );

    private static final TransferController transferController =
            new TransferController(
                    transferService
            );

    private static final TransactionController transactionController =
            new TransactionController(
                    transactionService
            );

    public static void main(String[] args) {

        System.out.println("Starting bank backend...");

        port(4567);

        ExceptionHandler.register();

       
        before((request, response) -> {

            response.header(
                    "Access-Control-Allow-Origin",
                    "http://localhost:3000"
            );

            response.header(
                    "Access-Control-Allow-Methods",
                    "GET,POST,PUT,DELETE,OPTIONS"
            );

            response.header(
                    "Access-Control-Allow-Headers",
                    "Content-Type"
            );
        });

        options("/*", (request, response) -> {
    return "";
});

        
        userController.registerRoutes();
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

