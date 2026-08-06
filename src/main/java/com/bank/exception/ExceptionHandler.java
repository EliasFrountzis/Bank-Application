package com.bank.exception;

import static spark.Spark.*;

import com.google.gson.Gson;

public class ExceptionHandler {

    private static final Gson gson = new Gson();


    public static void register() {


        exception(BankException.class, (exception, request, response) -> {


            response.type("application/json");


            response.status(
                    exception.getStatusCode()
            );


            response.body(
                    gson.toJson(
                            new ErrorResponse(
                                    exception.getMessage()
                            )
                    )
            );

        });


    }

}