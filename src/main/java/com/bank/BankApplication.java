package com.bank;

import static spark.Spark.*;



public class BankApplication {


    private static final AccountService accountService =
            new AccountService();

    private static final TransferService transferService =
            new TransferService(accountService);


            private static final TransferController transferController =
        new TransferController(transferService);


    public static void main(String[] args) {

    System.out.println("Starting bank backend...");

    port(4567);


    AccountController accountController =
            new AccountController(accountService);


    accountController.registerRoutes();
    transferController.registerRoutes();


    System.out.println("Server configured");


    try {
        Thread.currentThread().join();
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }

}
}