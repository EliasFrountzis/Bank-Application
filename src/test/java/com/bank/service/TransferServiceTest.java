package com.bank.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bank.repository.AccountRepository;
import com.bank.repository.InMemoryAccountRepository;
import com.bank.repository.InMemoryTransferRepository;
import com.bank.model.Account;
import com.bank.exception.BankException;


public class TransferServiceTest {


    private AccountService accountService;

    private TransferService transferService;

    private InMemoryTransferRepository transferRepository;



    @BeforeEach
    void setup() {


        AccountRepository accountRepository =
                new InMemoryAccountRepository();


        accountService =
                new AccountService(
                        accountRepository
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
                        "Alice",
                        1000
                );


        Account bob =
                accountService.createAccount(
                        "Bob",
                        500
                );


        transferRepository.addAccount(alice);
        transferRepository.addAccount(bob);



        transferService.transfer(
                alice.getId(),
                bob.getId(),
                200
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
                        "Alice",
                        100
                );


        Account bob =
                accountService.createAccount(
                        "Bob",
                        500
                );


        transferRepository.addAccount(alice);
        transferRepository.addAccount(bob);



        assertThrows(
                BankException.class,
                () -> transferService.transfer(
                        alice.getId(),
                        bob.getId(),
                        200
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
                        "Bob",
                        500
                );


        transferRepository.addAccount(bob);



        assertThrows(
                BankException.class,
                () -> transferService.transfer(
                        999,
                        bob.getId(),
                        100
                )
        );



        assertEquals(
                500,
                bob.getBalance()
        );

    }

}