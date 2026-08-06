package com.bank.controller;

import static spark.Spark.*;

import com.bank.service.AccountService;
import com.bank.dto.request.AccountRequest;
import com.bank.dto.response.AccountResponse;
import com.bank.model.Account;
import com.google.gson.Gson;
import java.util.List;

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

            return gson.toJson(
    new AccountResponse(account)
);

        });



        get("/accounts", (request, response) -> {

    response.type("application/json");

    List<AccountResponse> responses =
            accountService.getAccounts()
                    .stream()
                    .map(AccountResponse::new)
                    .toList();

    return gson.toJson(responses);

});


        get("/accounts/:id", (request, response) -> {

    int id = Integer.parseInt(request.params(":id"));

    Account account =
        accountService.getAccountById(id);

response.type("application/json");

return gson.toJson(
    new AccountResponse(account)
);

});

    }

}