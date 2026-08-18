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

        // CREATE ACCOUNT
        

        post("/accounts", (request, response) -> {

            AccountRequest accountRequest =
                    gson.fromJson(
                            request.body(),
                            AccountRequest.class
                    );


            Account account =
                    accountService.createAccount(
                            accountRequest.userId,
                            accountRequest.balance,
                            accountRequest.cardLast4,
                            accountRequest.name,
                            accountRequest.type
                    );


            response.status(201);
            response.type("application/json");


            return gson.toJson(
                    new AccountResponse(account)
            );
        });


        // GET ALL ACCOUNTS
      

        get("/accounts", (request, response) -> {

            response.type("application/json");


            List<AccountResponse> responses =
                    accountService.getAccounts()
                            .stream()
                            .map(AccountResponse::new)
                            .toList();


            return gson.toJson(responses);
        });


       
        // GET ACCOUNTS BY USER
       

        get(
                "/users/:userId/accounts",
                (request, response) -> {

                    int userId =
                            Integer.parseInt(
                                    request.params(":userId")
                            );


                    response.type("application/json");


                    List<AccountResponse> responses =
                            accountService
                                    .getAccountsByUserId(userId)
                                    .stream()
                                    .map(AccountResponse::new)
                                    .toList();


                    return gson.toJson(responses);
                }
        );


      
        // GET ACCOUNT BY ID
        

        get("/accounts/:id", (request, response) -> {

            int id =
                    Integer.parseInt(
                            request.params(":id")
                    );


            Account account =
                    accountService.getAccountById(id);


            response.type("application/json");


            return gson.toJson(
                    new AccountResponse(account)
            );
        });


     
        // DEPOSIT
      

        post(
                "/accounts/:id/deposit",
                (request, response) -> {

                    int accountId =
                            Integer.parseInt(
                                    request.params(":id")
                            );


                    double amount =
                            Double.parseDouble(
                                    request.body()
                            );


                    Account account =
                            accountService.deposit(
                                    accountId,
                                    amount
                            );


                    response.status(200);
                    response.type("application/json");


                    return gson.toJson(
                            new AccountResponse(account)
                    );
                }
        );


       
        // WITHDRAW
        

        post(
                "/accounts/:id/withdraw",
                (request, response) -> {

                    int accountId =
                            Integer.parseInt(
                                    request.params(":id")
                            );


                    double amount =
                            Double.parseDouble(
                                    request.body()
                            );


                    Account account =
                            accountService.withdraw(
                                    accountId,
                                    amount
                            );


                    response.status(200);
                    response.type("application/json");


                    return gson.toJson(
                            new AccountResponse(account)
                    );
                }
        );

        // CLOSE ACCOUNT

post(
        "/accounts/:id/close",
        (request, response) -> {

            int accountId =
                    Integer.parseInt(
                            request.params(":id")
                    );

            Account account =
                    accountService.closeAccount(
                            accountId
                    );

            response.status(200);
            response.type("application/json");

            return gson.toJson(
                    new AccountResponse(account)
            );
        }
);
    }
}

