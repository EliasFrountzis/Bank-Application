package com.bank.controller;

import static spark.Spark.*;

import com.bank.exception.ExceptionHandler;
import com.bank.repository.InMemoryTransactionRepository;
import com.bank.repository.TransactionRepository;
import com.bank.service.TransactionService;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;


public class TransactionControllerTest {

    private TransactionService transactionService;


    @BeforeEach
    void setup() {

        // Reset Spark before every test
        stop();
        awaitStop();


        port(4568);


        TransactionRepository repository =
                new InMemoryTransactionRepository();


        transactionService =
                new TransactionService(repository);


        TransactionController controller =
                new TransactionController(transactionService);


        ExceptionHandler.register();


        controller.registerRoutes();


        awaitInitialization();
    }


    @AfterEach
    void cleanup() {

        stop();
        awaitStop();

    }



    @Test
    void shouldReturnTransactions() throws IOException {


        HttpGet request =
                new HttpGet(
                        "http://localhost:4568/transactions"
                );


        try(CloseableHttpClient client =
                HttpClients.createDefault()) {


           client.execute(request, response -> {

    assertEquals(
            200,
            response.getCode()
    );

    return null;
});

        }

    }



    @Test
void shouldReturnTransactionsForAccount() throws IOException {


    transactionService.createTransaction(
            1,
            2,
            100
    );


    HttpGet request =
            new HttpGet(
                    "http://localhost:4568/accounts/1/transactions"
            );


    try(CloseableHttpClient client =
            HttpClients.createDefault()) {


        client.execute(request, response -> {


            assertEquals(
                    200,
                    response.getCode()
            );


            String body =
                    new String(
                            response.getEntity()
                                    .getContent()
                                    .readAllBytes(),
                            StandardCharsets.UTF_8
                    );


            assertTrue(
                    body.contains("\"fromAccount\":1")
            );


            assertTrue(
                    body.contains("\"amount\":100")
            );


            return null;

        });

    }

}
}