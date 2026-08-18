package com.bank.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static spark.Spark.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bank.exception.ExceptionHandler;
import com.bank.model.Account;
import com.bank.model.Transaction;
import com.bank.model.User;
import com.bank.repository.AccountRepository;
import com.bank.repository.InMemoryAccountRepository;
import com.bank.repository.InMemoryTransferRepository;
import com.bank.repository.TransactionRepository;
import com.bank.repository.UserRepository;
import com.bank.service.AccountService;
import com.bank.service.TransferService;
import com.bank.service.UserService;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;

public class TransferControllerTest {

    private Account alice;
    private Account bob;

    @BeforeEach
    void setup() {

        stop();
        awaitStop();

        port(4567);

        ExceptionHandler.register();

        UserRepository userRepository =
                new UserRepository() {

                    private final List<User> users =
                            new ArrayList<>();

                    private int nextId = 1;

                    @Override
                    public User save(User user) {

                        User saved =
                                new User(
                                        nextId++,
                                        user.getName(),
                                        user.getEmail(),
                                        user.getPasswordHash()
                                );

                        users.add(saved);

                        return saved;
                    }

                    @Override
                    public User findById(int id) {

                        return users.stream()
                                .filter(
                                        user ->
                                                user.getId() == id
                                )
                                .findFirst()
                                .orElse(null);
                    }

                    @Override
                    public User findByEmail(String email) {

                        return users.stream()
                                .filter(
                                        user ->
                                                user.getEmail()
                                                        .equalsIgnoreCase(email)
                                )
                                .findFirst()
                                .orElse(null);
                    }

                    @Override
                    public User findByName(String name) {

                        return users.stream()
                                .filter(
                                        user ->
                                                user.getName()
                                                        .equalsIgnoreCase(name)
                                )
                                .findFirst()
                                .orElse(null);
                    }

                    @Override
                    public List<User> findAll() {

                        return users;
                    }
                };

        UserService userService =
                new UserService(
                        userRepository
                );

        userService.createUser(
                "Alice",
                "alice@test.com",
                "password"
        );

        userService.createUser(
                "Bob",
                "bob@test.com",
                "password"
        );

        AccountRepository accountRepository =
                new InMemoryAccountRepository();

        TransactionRepository transactionRepository =
                new TransactionRepository() {

                    private final List<Transaction> transactions =
                            new ArrayList<>();

                    @Override
                    public Transaction save(
                            Transaction transaction
                    ) {

                        transactions.add(transaction);

                        return transaction;
                    }

                    @Override
                    public List<Transaction> findAll() {

                        return transactions;
                    }

                    @Override
                    public List<Transaction> findByAccountId(
                            int accountId
                    ) {

                        return transactions.stream()
                                .filter(
                                        transaction ->
                                                transaction.getAccountId()
                                                        == accountId
                                )
                                .toList();
                    }
                };

        AccountService accountService =
                new AccountService(
                        accountRepository,
                        userService,
                        transactionRepository
                );

        InMemoryTransferRepository transferRepository =
                new InMemoryTransferRepository();

        TransferService transferService =
                new TransferService(
                        accountService,
                        transferRepository
                );

        User aliceUser =
                userService.getUserById(1);

        User bobUser =
                userService.getUserById(2);

        alice =
        accountService.createAccount(
                aliceUser.getId(),
                1000,
                "1234",
                "Alice Current",
                "CURRENT"
        );

bob =
        accountService.createAccount(
                bobUser.getId(),
                500,
                "5678",
                "Bob Current",
                "CURRENT"
        );

        transferRepository.addAccount(alice);
        transferRepository.addAccount(bob);

        TransferController controller =
                new TransferController(
                        transferService
                );

        controller.registerRoutes();

        awaitInitialization();
    }

    @AfterEach
    void tearDown() {

        stop();
        awaitStop();
    }

    @Test
    void shouldTransferMoneySuccessfully() throws Exception {

        try (CloseableHttpClient client =
                     HttpClients.createDefault()) {

            HttpPost request =
                    new HttpPost(
                            "http://localhost:4567/transfers"
                    );

            request.setHeader(
                    "Content-Type",
                    "application/json"
            );

            String json =
                    """
                    {
                        "fromAccount": 1,
                        "toAccount": 2,
                        "amount": 100,
                        "description": "Groceries"
                    }
                    """;

            request.setEntity(
                    new StringEntity(json)
            );

            client.execute(
                    request,
                    response -> {

                        assertEquals(
                                200,
                                response.getCode()
                        );

                        return null;
                    }
            );
        }

        assertEquals(
                900,
                alice.getBalance()
        );

        assertEquals(
                600,
                bob.getBalance()
        );
    }
}