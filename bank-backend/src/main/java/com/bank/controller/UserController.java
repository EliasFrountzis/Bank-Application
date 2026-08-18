package com.bank.controller;

import static spark.Spark.*;

import com.bank.dto.request.LoginRequest;
import com.bank.dto.request.UserRequest;
import com.bank.dto.response.UserResponse;
import com.bank.model.User;
import com.bank.service.UserService;
import com.google.gson.Gson;

public class UserController {

    private final UserService userService;
    private final Gson gson;

    public UserController(
            UserService userService,
            Gson gson
    ) {
        this.userService = userService;
        this.gson = gson;
    }

    public void registerRoutes() {

        // =========================
        // REGISTER
        // =========================

        post("/users", (request, response) -> {

            UserRequest userRequest =
                    gson.fromJson(
                            request.body(),
                            UserRequest.class
                    );

            User user =
                    userService.createUser(
                            userRequest.getName(),
                            userRequest.getEmail(),
                            userRequest.getPassword()
                    );

            response.status(201);
            response.type("application/json");

            return gson.toJson(
                    new UserResponse(user)
            );
        });


        // =========================
        // LOGIN
        // =========================

        post("/login", (request, response) -> {

            LoginRequest loginRequest =
                    gson.fromJson(
                            request.body(),
                            LoginRequest.class
                    );

            User user =
                    userService.login(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    );

            response.status(200);
            response.type("application/json");

            return gson.toJson(
                    new UserResponse(user)
            );
        });


        // =========================
        // GET USER BY EMAIL
        // =========================

        get("/users/email", (request, response) -> {

            String email =
                    request.queryParams("email");

            User user =
                    userService.getUserByEmail(email);

            response.status(200);
            response.type("application/json");

            return gson.toJson(
                    new UserResponse(user)
            );
        });


        // =========================
        // GET USER BY NAME
        // =========================

        get("/users/name", (request, response) -> {

            String name =
                    request.queryParams("name");

            User user =
                    userService.getUserByName(name);

            response.status(200);
            response.type("application/json");

            return gson.toJson(
                    new UserResponse(user)
            );
        });


        // =========================
        // GET USER BY ID
        // KEEP LAST
        // =========================

        get("/users/:id", (request, response) -> {

            int id =
                    Integer.parseInt(
                            request.params(":id")
                    );

            User user =
                    userService.getUserById(id);

            response.status(200);
            response.type("application/json");

            return gson.toJson(
                    new UserResponse(user)
            );
        });
    }
}