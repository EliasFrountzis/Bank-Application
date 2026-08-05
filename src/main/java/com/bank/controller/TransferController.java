package com.bank.controller;

import static spark.Spark.*;

import com.bank.service.TransferService;
import com.bank.request.TransferRequest;
import com.google.gson.Gson;

public class TransferController {

    private final Gson gson = new Gson();
    private final TransferService transferService;


    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }


    public void registerRoutes() {

        post("/transfer", (request, response) -> {

    TransferRequest transferRequest =
            gson.fromJson(request.body(), TransferRequest.class);


    transferService.transfer(
            transferRequest.fromAccount,
            transferRequest.toAccount,
            transferRequest.amount
    );


    response.type("application/json");

    return gson.toJson("Transfer completed");

});

    }

}