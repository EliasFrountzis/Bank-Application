package com.bank.controller;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import com.bank.exception.ExceptionHandler;
import com.bank.repository.AccountRepository;
import com.bank.repository.InMemoryAccountRepository;
import com.bank.repository.InMemoryTransactionRepository;
import com.bank.repository.TransactionRepository;

import com.bank.service.AccountService;
import com.bank.service.TransactionService;
import com.bank.service.TransferService;


import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;






public class TransferControllerTest {


    @BeforeAll
static void setup(){

   

    ExceptionHandler.register();

    




        AccountRepository accountRepository =
                new InMemoryAccountRepository();


        TransactionRepository transactionRepository =
                new InMemoryTransactionRepository();



        AccountService accountService =
                new AccountService(accountRepository);



        TransactionService transactionService =
                new TransactionService(transactionRepository);



        TransferService transferService =
                new TransferService(
                        accountService,
                        transactionService
                );



        // Create test accounts

        accountService.createAccount(
                "Alice",
                1000
        );


        accountService.createAccount(
                "Bob",
                500
        );



        TransferController controller =
                new TransferController(
                        transferService
                );



       


        ExceptionHandler.register();


        controller.registerRoutes();

    }




    @Test
void shouldTransferMoneySuccessfully() throws Exception {


    try(CloseableHttpClient client =
            HttpClients.createDefault()){


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