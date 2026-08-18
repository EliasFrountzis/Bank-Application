package com.bank.controller;

import static spark.Spark.*;

import com.bank.dto.request.TransferRequest;
import com.bank.service.TransferService;
import com.google.gson.Gson;

public class TransferController {

    private final Gson gson = new Gson();
    private final TransferService transferService;


    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }


    public void registerRoutes() {

       post("/transfers", (request, response) -> {

    try {

        TransferRequest transferRequest =
                gson.fromJson(request.body(), TransferRequest.class);


       transferService.transfer(
        transferRequest.fromAccount,
        transferRequest.toAccount,
        transferRequest.amount,
        transferRequest.description
);


       response.status(200);
response.type("application/json");

return """
{
    "message": "Transfer completed successfully"
}
""";
    } catch(Exception e) {

        e.printStackTrace();

        throw e;

    }

});

    }

}