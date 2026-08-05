package com.bank.controller;

import static spark.Spark.*;

import com.bank.service.TransactionService;
import com.google.gson.Gson;

public class TransactionController {

    private final TransactionService transactionService;
    private final Gson gson = new Gson();


    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }


    public void registerRoutes() {

        get("/transactions", (request, response) -> {

            response.type("application/json");

            return gson.toJson(transactionService.getTransactions());

        });

    }

}