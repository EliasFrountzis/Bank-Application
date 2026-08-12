package com.bank.controller;

import static spark.Spark.*;

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

            return gson.toJson(new UserResponse(user));
        });

        get("/users/:id", (request, response) -> {

            int id =
                    Integer.parseInt(
                            request.params(":id")
                    );

            User user =
                    userService.getUserById(id);

            response.status(200);

            return gson.toJson(new UserResponse(user));
        });
    }
}

