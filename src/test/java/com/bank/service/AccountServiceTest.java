package com.bank.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bank.exception.BankException;
import com.bank.model.Account;
import com.bank.model.User;
import com.bank.repository.AccountRepository;
import com.bank.repository.InMemoryAccountRepository;
import com.bank.repository.InMemoryTransactionRepository;
import com.bank.repository.TransactionRepository;
import com.bank.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class AccountServiceTest {

    private AccountService accountService;


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
                                                        .equals(email)
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
                new UserService(userRepository);


        accountService =
                new AccountService(
                        accountRepository,
                        userService,
                        transactionRepository
                );


        userRepository.save(
                new User(
                        1,
                        "Test User",
                        "test@example.com",
                        "password"
                )
        );
    }


    @Test
    void shouldCreateAccount() {

        Account account =
                accountService.createAccount(
                        1,
                        1000,
                        "1234",
                        "My Current Account",
                        "CURRENT"
                );


        assertEquals(
                1,
                account.getUserId()
        );


        assertEquals(
                1000,
                account.getBalance()
        );


        assertEquals(
                "1234",
                account.getCardLast4()
        );


        assertEquals(
                "My Current Account",
                account.getName()
        );


        assertEquals(
                "CURRENT",
                account.getType()
        );
    }


    @Test
    void shouldNotCreateAccountWithoutValidUserId() {

        assertThrows(
                BankException.class,
                () -> {

                    accountService.createAccount(
                            0,
                            1000,
                            "1234",
                            "Test Account",
                            "CURRENT"
                    );
                }
        );
    }


    @Test
    void shouldNotCreateAccountWithNegativeBalance() {

        assertThrows(
                BankException.class,
                () -> {

                    accountService.createAccount(
                            1,
                            -100,
                            "1234",
                            "Test Account",
                            "CURRENT"
                    );
                }
        );
    }


    @Test
    void shouldFindAccountById() {

        Account account =
                accountService.createAccount(
                        1,
                        1000,
                        "1234",
                        "My Savings",
                        "SAVINGS"
                );


        Account found =
                accountService.getAccountById(
                        account.getId()
                );


        assertNotNull(found);


        assertEquals(
                account.getId(),
                found.getId()
        );


        assertEquals(
                1,
                found.getUserId()
        );


        assertEquals(
                1000,
                found.getBalance()
        );


        assertEquals(
                "1234",
                found.getCardLast4()
        );


        assertEquals(
                "My Savings",
                found.getName()
        );


        assertEquals(
                "SAVINGS",
                found.getType()
        );
    }


    // =========================
    // NEW TEST
    // =========================

    @Test
    void shouldNotCreateAccountWithEmptyName() {

        assertThrows(
                BankException.class,
                () -> {

                    accountService.createAccount(
                            1,
                            1000,
                            "1234",
                            "",
                            "CURRENT"
                    );
                }
        );
    }


    // =========================
    // NEW TEST
    // =========================

    @Test
    void shouldNotCreateAccountWithInvalidType() {

        assertThrows(
                BankException.class,
                () -> {

                    accountService.createAccount(
                            1,
                            1000,
                            "1234",
                            "Test Account",
                            "INVESTMENT"
                    );
                }
        );
    }
}