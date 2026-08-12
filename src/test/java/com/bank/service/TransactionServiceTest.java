package com.bank.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bank.model.Transaction;
import com.bank.repository.TransactionRepository;
import com.bank.repository.InMemoryTransactionRepository;

public class TransactionServiceTest {

    private TransactionService transactionService;

    @BeforeEach
    void setup() {

        TransactionRepository repository =
                new InMemoryTransactionRepository();

        transactionService =
                new TransactionService(repository);
    }

    @Test
    void shouldCreateTransaction() {

        Transaction transaction =
                transactionService.createTransaction(
                        1,
                        2,
                        200,
                        "Groceries"
                );

        assertEquals(
                1,
                transaction.getFromAccount()
        );

        assertEquals(
                2,
                transaction.getToAccount()
        );

        assertEquals(
                200,
                transaction.getAmount()
        );

        assertEquals(
                "Groceries",
                transaction.getDescription()
        );
    }

    @Test
    void shouldReturnAllTransactions() {

        transactionService.createTransaction(
                1,
                2,
                200,
                "Groceries"
        );

        transactionService.createTransaction(
                2,
                3,
                50,
                "Dinner"
        );

        assertEquals(
                2,
                transactionService
                        .getTransactions()
                        .size()
        );

        assertEquals(
                200,
                transactionService
                        .getTransactions()
                        .get(0)
                        .getAmount()
        );

        assertEquals(
                50,
                transactionService
                        .getTransactions()
                        .get(1)
                        .getAmount()
        );
    }
}

