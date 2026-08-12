package com.bank.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bank.repository.AccountRepository;
import com.bank.repository.InMemoryAccountRepository;
import com.bank.repository.InMemoryTransactionRepository;
import com.bank.repository.InMemoryTransferRepository;
import com.bank.repository.TransactionRepository;
import com.bank.repository.UserRepository;

import com.bank.model.Account;
import com.bank.model.User;
import com.bank.exception.BankException;

import java.util.ArrayList;
import java.util.List;

public class TransferServiceTest {

    private AccountService accountService;

    private TransferService transferService;

    private InMemoryTransferRepository transferRepository;

    @BeforeEach
    void setup() {

        AccountRepository accountRepository =
                new InMemoryAccountRepository();

        TransactionRepository transactionRepository =
                new InMemoryTransactionRepository();

        UserRepository userRepository =
                new UserRepository() {

                    private final List<User> users =
                            new ArrayList<>();

                    @Override
                    public User save(User user) {
                        users.add(user);
                        return user;
                    }

                    @Override
                    public User findById(int id) {

                        return users.stream()
                                .filter(user ->
                                        user.getId() == id)
                                .findFirst()
                                .orElse(null);
                    }

                    @Override
                    public User findByEmail(String email) {

                        return users.stream()
                                .filter(user ->
                                        user.getEmail()
                                                .equals(email))
                                .findFirst()
                                .orElse(null);
                    }

                    @Override
                    public List<User> findAll() {
                        return users;
                    }
                };

        UserService userService =
                new UserService(userRepository);

        accountService =
                new AccountService(
                        accountRepository,
                        userService,
                        transactionRepository
                );

        // Users required by AccountService.createAccount()
        userRepository.save(
                new User(
                        1,
                        "Alice",
                        "alice@example.com",
                        "password"
                )
        );

        userRepository.save(
                new User(
                        2,
                        "Bob",
                        "bob@example.com",
                        "password"
                )
        );

        transferRepository =
                new InMemoryTransferRepository();

        transferService =
                new TransferService(
                        accountService,
                        transferRepository
                );
    }

    @Test
    void shouldTransferMoneySuccessfully() {

        Account alice =
                accountService.createAccount(
                        1,
                        1000,
                        "1234"
                );

        Account bob =
                accountService.createAccount(
                        2,
                        500,
                        "5678"
                );

        transferRepository.addAccount(alice);
        transferRepository.addAccount(bob);

        transferService.transfer(
                alice.getId(),
                bob.getId(),
                200,
                "Groceries"
        );

        assertEquals(
                800,
                alice.getBalance()
        );

        assertEquals(
                700,
                bob.getBalance()
        );
    }

    @Test
    void shouldNotTransferWhenInsufficientFunds() {

        Account alice =
                accountService.createAccount(
                        1,
                        100,
                        "1234"
                );

        Account bob =
                accountService.createAccount(
                        2,
                        500,
                        "5678"
                );

        transferRepository.addAccount(alice);
        transferRepository.addAccount(bob);

        assertThrows(
                BankException.class,
                () -> transferService.transfer(
                        alice.getId(),
                        bob.getId(),
                        200,
                        "Groceries"
                )
        );

        assertEquals(
                100,
                alice.getBalance()
        );

        assertEquals(
                500,
                bob.getBalance()
        );
    }

    @Test
    void shouldNotTransferWhenAccountDoesNotExist() {

        Account bob =
                accountService.createAccount(
                        2,
                        500,
                        "5678"
                );

        transferRepository.addAccount(bob);

        assertThrows(
                BankException.class,
                () -> transferService.transfer(
                        999,
                        bob.getId(),
                        100,
                        "Groceries"
                )
        );

        assertEquals(
                500,
                bob.getBalance()
        );
    }
}

