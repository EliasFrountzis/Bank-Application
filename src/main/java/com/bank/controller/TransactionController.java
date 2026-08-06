package com.bank.controller;

import static spark.Spark.*;

import com.bank.dto.response.TransactionResponse;
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

    return gson.toJson(
        transactionService.getTransactions()
            .stream()
            .map(TransactionResponse::new)
            .toList()
    );

});

       get("/accounts/:id/transactions", (request, response) -> {


    int accountId =
            Integer.parseInt(
                    request.params(":id")
            );


    response.type("application/json");


    return gson.toJson(
    transactionService.getTransactionsByAccount(accountId)
        .stream()
        .map(TransactionResponse::new)
        .toList()
);
});

    }

}