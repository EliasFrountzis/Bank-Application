package com.bank;

import static spark.Spark.*;

public class BankApplication {

    public static void main(String[] args) {

        System.out.println("Starting bank backend...");

        port(4567);

        get("/hello", (request, response) -> {
            return "Welcome to Bank Backend";
        });

        System.out.println("Server configured");

        // Keep application alive
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}