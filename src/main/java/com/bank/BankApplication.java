package com.bank;

import static spark.Spark.*;

public class BankApplication {

    public static void main(String[] args) {

        System.out.println("Starting bank backend...");

        port(4567);

        AccountService accountService = new AccountService();

        AccountController accountController =
                new AccountController(accountService);

        accountController.registerRoutes();


        System.out.println("Server configured");


        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }
}