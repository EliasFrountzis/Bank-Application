package com.bank.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.bank.exception.ExceptionHandler;
import com.bank.repository.InMemoryUserRepository;
import com.bank.repository.UserRepository;
import com.bank.service.UserService;
import com.google.gson.Gson;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;

import org.junit.jupiter.api.*;

import static spark.Spark.*;

public class UserControllerTest {

    private UserService userService;

    @BeforeEach
    void setup() {

        stop();
        awaitStop();

        port(4569);

        UserRepository repository =
                new InMemoryUserRepository();

        userService =
                new UserService(repository);

        UserController controller =
                new UserController(
                        userService,
                        new Gson()
                );

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
    void shouldCreateUser() throws Exception {

        HttpPost request =
                new HttpPost(
                        "http://localhost:4569/users"
                );

        request.setEntity(
                new StringEntity(
                        """
                        {
                            "name": "Ilias",
                            "email": "ilias@test.com",
                            "password": "password"
                        }
                        """,
                        ContentType.APPLICATION_JSON
                )
        );

        try (CloseableHttpClient client =
                     HttpClients.createDefault()) {

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
    void shouldGetUserById() throws Exception {

        var user =
                userService.createUser(
                        "Ilias",
                        "ilias@test.com",
                        "password"
                );

        HttpGet request =
                new HttpGet(
                        "http://localhost:4569/users/"
                                + user.getId()
                );

        try (CloseableHttpClient client =
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
                                        .readAllBytes()
                        );

                assertTrue(
                        body.contains("\"name\":\"Ilias\"")
                );

                assertTrue(
                        body.contains(
                                "\"email\":\"ilias@test.com\""
                        )
                );

                return null;
            });
        }
    }

    @Test
    void shouldReturnBadRequestForInvalidUser() throws Exception {

        HttpPost request =
                new HttpPost(
                        "http://localhost:4569/users"
                );

        request.setEntity(
                new StringEntity(
                        """
                        {
                            "name": "",
                            "email": "ilias@test.com",
                            "password": "password"
                        }
                        """,
                        ContentType.APPLICATION_JSON
                )
        );

        try (CloseableHttpClient client =
                     HttpClients.createDefault()) {

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
    void shouldReturnNotFoundForMissingUser() throws Exception {

        HttpGet request =
                new HttpGet(
                        "http://localhost:4569/users/999"
                );

        try (CloseableHttpClient client =
                     HttpClients.createDefault()) {

            client.execute(request, response -> {

                assertEquals(
                        404,
                        response.getCode()
                );

                return null;
            });
        }
    }

    @Test
    void shouldRejectDuplicateEmail() throws Exception {

        userService.createUser(
                "Ilias",
                "ilias@test.com",
                "password"
        );

        HttpPost request =
                new HttpPost(
                        "http://localhost:4569/users"
                );

        request.setEntity(
                new StringEntity(
                        """
                        {
                            "name": "Another User",
                            "email": "ilias@test.com",
                            "password": "password"
                        }
                        """,
                        ContentType.APPLICATION_JSON
                )
        );

        try (CloseableHttpClient client =
                     HttpClients.createDefault()) {

            client.execute(request, response -> {

                assertEquals(
                        400,
                        response.getCode()
                );

                return null;
            });
        }
    }
}