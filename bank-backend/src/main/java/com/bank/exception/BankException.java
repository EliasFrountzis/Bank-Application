package com.bank.exception;

public class BankException extends RuntimeException {

    private final int statusCode;


    public BankException(String message, int statusCode) {

        super(message);
        this.statusCode = statusCode;

    }


    public int getStatusCode() {

        return statusCode;

    }

}