package com.bank;

import static spark.Spark.*;

import com.google.gson.Gson;

public class AccountController {

    private final AccountService accountService;
    private final Gson gson = new Gson();


    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }


    public void registerRoutes() {


        post("/accounts", (request, response) -> {

            AccountRequest accountRequest =
                    gson.fromJson(request.body(), AccountRequest.class);


            Account account =
                    accountService.createAccount(
                            accountRequest.owner,
                            accountRequest.balance
                    );

                      response.status(201);


            response.type("application/json");

            return gson.toJson(account);

        });



        get("/accounts", (request, response) -> {

            response.type("application/json");

            return gson.toJson(accountService.getAccounts());

        });


        get("/accounts/:id", (request, response) -> {

    int id = Integer.parseInt(request.params(":id"));

    Account account = accountService.getAccountById(id);

    response.type("application/json");

     if(account == null){

        response.status(404);

        return gson.toJson(
                "Account not found"
        );
    }

    return gson.toJson(account);

});

    }

}