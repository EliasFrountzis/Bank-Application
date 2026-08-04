package com.bank;

import static spark.Spark.*;

import com.google.gson.Gson;

public class BankApplication {

    private static final Gson gson = new Gson();
private static final AccountService accountService = new AccountService();

    public static void main(String[] args) {

        System.out.println("Starting bank backend...");

        port(4567);



        get("/hello", (request, response) -> {
            return "Welcome to Bank Backend";
        });



post("/accounts", (request, response) -> {


    AccountRequest accountRequest =
            gson.fromJson(request.body(), AccountRequest.class);

    Account account =
            accountService.createAccount(
                    accountRequest.owner,
                    accountRequest.balance
            );

    response.type("application/json");

    return gson.toJson(account);
});

get("/accounts", (request, response) -> {

    response.type("application/json");

    return gson.toJson(accountService.getAccounts());

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