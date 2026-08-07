package com.bank.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static spark.Spark.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bank.exception.ExceptionHandler;
import com.bank.model.Account;
import com.bank.repository.AccountRepository;
import com.bank.repository.InMemoryAccountRepository;
import com.bank.repository.InMemoryTransferRepository;
import com.bank.repository.TransferRepository;
import com.bank.service.AccountService;
import com.bank.service.TransferService;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;


public class TransferControllerTest {


    @BeforeEach
void setup() {


    port(4567);

    ExceptionHandler.register();


    AccountRepository accountRepository =
            new InMemoryAccountRepository();


    AccountService accountService =
            new AccountService(
                    accountRepository
            );


    InMemoryTransferRepository transferRepository =
            new InMemoryTransferRepository();



    TransferService transferService =
            new TransferService(
                    accountService,
                    transferRepository
            );



    Account alice =
            accountService.createAccount(
                    "Alice",
                    1000
            );


    Account bob =
            accountService.createAccount(
                    "Bob",
                    500
            );


    transferRepository.addAccount(alice);
    transferRepository.addAccount(bob);



    TransferController controller =
            new TransferController(
                    transferService
            );


    controller.registerRoutes();


    awaitInitialization();

}


    @AfterEach
    void tearDown() {

        stop();
        awaitStop();

    }



    @Test
    void shouldTransferMoneySuccessfully() throws Exception {


        try (CloseableHttpClient client =
                     HttpClients.createDefault()) {


            HttpPost request =
                    new HttpPost(
                            "http://localhost:4567/transfer"
                    );


            request.setHeader(
                    "Content-Type",
                    "application/json"
            );


            String json =
                    """
                    {
                        "fromAccount":1,
                        "toAccount":2,
                        "amount":100
                    }
                    """;


            request.setEntity(
                    new StringEntity(json)
            );


            client.execute(request, response -> {


                assertEquals(
                        200,
                        response.getCode()
                );


                return null;

            });

        }

    }

}