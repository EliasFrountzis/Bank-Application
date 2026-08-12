package com.bank.controller;

import static org.junit.jupiter.api.Assertions.*;
import static spark.Spark.*;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bank.exception.ExceptionHandler;
import com.bank.model.User;
import com.bank.repository.AccountRepository;
import com.bank.repository.InMemoryAccountRepository;
import com.bank.repository.InMemoryTransactionRepository;
import com.bank.repository.PostgresUserRepository;
import com.bank.repository.TransactionRepository;
import com.bank.repository.UserRepository;
import com.bank.service.AccountService;
import com.bank.service.UserService;

public class AccountControllerTest {

    private int testUserId;

    @BeforeEach
    void setup() {

        stop();
        awaitStop();

        port(4567);

        ExceptionHandler.register();

        // User service using the real PostgreSQL repository
        UserRepository userRepository =
                new PostgresUserRepository();

        UserService userService =
                new UserService(userRepository);

        // Create a unique test user
        User user =
                userService.createUser(
                        "Account Test User",
                        "account-test-" + System.nanoTime() + "@test.com",
                        "test-password"
                );

        testUserId = user.getId();

        // In-memory repositories for the account test
        AccountRepository accountRepository =
                new InMemoryAccountRepository();

        TransactionRepository transactionRepository =
                new InMemoryTransactionRepository();

        AccountService service =
                new AccountService(
                        accountRepository,
                        userService,
                        transactionRepository
                );

        // Create initial account
        service.createAccount(
                testUserId,
                1000,
                "1234"
        );

        AccountController controller =
                new AccountController(service);

        controller.registerRoutes();

        awaitInitialization();
    }

    @AfterEach
    void cleanup() {

        stop();
        awaitStop();
    }


    @Test
    void shouldCreateAccountThroughAPI() throws Exception {

        try (CloseableHttpClient client =
                     HttpClients.createDefault()) {

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
                        "userId": %d,
                        "balance": 1000,
                        "cardLast4": "5678"
                    }
                    """.formatted(testUserId);

            request.setEntity(
                    new StringEntity(json)
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

        try (CloseableHttpClient client =
                     HttpClients.createDefault()) {

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

        try (CloseableHttpClient client =
                     HttpClients.createDefault()) {

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

        try (CloseableHttpClient client =
                     HttpClients.createDefault()) {

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
                        "userId": %d,
                        "balance": -100,
                        "cardLast4": "1234"
                    }
                    """.formatted(testUserId);

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

        try (CloseableHttpClient client =
                     HttpClients.createDefault()) {

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

