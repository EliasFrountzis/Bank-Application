package com.bank.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bank.exception.BankException;
import com.bank.model.Account;
import com.bank.repository.AccountRepository;
import com.bank.repository.InMemoryAccountRepository;



public class AccountServiceTest {


    private AccountService accountService;


    @BeforeEach
    void setup() {

        AccountRepository repository =
                new InMemoryAccountRepository();


        accountService =
                new AccountService(repository);

    }


    @Test
    void shouldCreateAccount() {

        Account account =
                accountService.createAccount(
                        "Alice",
                        1000
                );


        assertEquals(
                "Alice",
                account.getOwner()
        );


        assertEquals(
                1000,
                account.getBalance()
        );

    }


    @Test
void shouldNotCreateAccountWithoutOwner(){

    assertThrows(
        BankException.class,
        () -> {
            accountService.createAccount(
                "",
                1000
            );
        }
    );

}


@Test
void shouldNotCreateAccountWithNegativeBalance(){

    assertThrows(
      BankException.class,
        () -> {
            accountService.createAccount(
                "Alice",
                -100
            );
        }
    );

}


@Test
void shouldFindAccountById(){

    Account account =
            accountService.createAccount(
                    "Alice",
                    1000
            );


    Account found =
            accountService.getAccountById(
                    account.getId()
            );


    assertEquals(
            "Alice",
            found.getOwner()
    );

}

}