package com.bank.controller;


import static org.junit.jupiter.api.Assertions.*;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import com.bank.exception.ExceptionHandler;



import static spark.Spark.*;


import com.bank.repository.AccountRepository;
import com.bank.repository.InMemoryAccountRepository;
import com.bank.service.AccountService;



public class AccountControllerTest {


    @BeforeAll
static void setup(){

    stop();

    port(4567);

    ExceptionHandler.register();




    AccountRepository repository =
            new InMemoryAccountRepository();


    AccountService service =
            new AccountService(repository);


    service.createAccount(
            "Alice",
            1000
    );


    AccountController controller =
            new AccountController(service);


    port(4567);


    ExceptionHandler.register();


    controller.registerRoutes();

}



    @Test
void shouldCreateAccountThroughAPI() throws Exception {


    try(CloseableHttpClient client =
            HttpClients.createDefault()){


        HttpPost request =
                new HttpPost(
                    "http://localhost:4567/accounts"
                );


        request.setHeader(
                "Content-Type",
                "application/json"
        );


        String json =
                """
                {
                    "owner":"Alice",
                    "balance":1000
                }
                """;


        request.setEntity(
                new org.apache.hc.core5.http.io.entity.StringEntity(json)
        );


       client.execute(request, response -> {

    assertEquals(
            201,
            response.getCode()
    );

    return null;
});
    }

}




@Test
void shouldGetAccountsThroughAPI() throws Exception {


    try(CloseableHttpClient client =
            HttpClients.createDefault()){


        HttpGet request =
                new HttpGet(
                    "http://localhost:4567/accounts"
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




@Test
void shouldGetAccountByIdThroughAPI() throws Exception {


    try(CloseableHttpClient client =
            HttpClients.createDefault()){


        HttpGet request =
                new HttpGet(
                    "http://localhost:4567/accounts/1"
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




@Test
void shouldRejectInvalidAccountCreation() throws Exception {


    try(CloseableHttpClient client =
            HttpClients.createDefault()){


        HttpPost request =
                new HttpPost(
                    "http://localhost:4567/accounts"
                );


        request.setHeader(
                "Content-Type",
                "application/json"
        );


        String json =
                """
                {
                    "owner":"",
                    "balance":1000
                }
                """;


        request.setEntity(
            new StringEntity(json)
        );


        client.execute(request, response -> {


            assertEquals(
                    400,
                    response.getCode()
            );


            return null;
        });

    }

}


@Test
void shouldReturn404WhenAccountDoesNotExist() throws Exception {


    try(CloseableHttpClient client =
            HttpClients.createDefault()){


        HttpGet request =
                new HttpGet(
                    "http://localhost:4567/accounts/999"
                );


        client.execute(request, response -> {


            assertEquals(
                    404,
                    response.getCode()
            );


            return null;
        });

    }

}

}