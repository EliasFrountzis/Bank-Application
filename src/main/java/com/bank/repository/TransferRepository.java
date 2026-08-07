package com.bank.repository;

public interface TransferRepository {

    void transfer(
            int fromAccount,
            int toAccount,
            double amount
    );

}