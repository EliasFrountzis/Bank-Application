package com.bank.repository;

import com.bank.database.PostgresIntegrationTest;
import com.bank.model.Account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

public class PostgresTransferRepositoryTest
        extends PostgresIntegrationTest {

    @BeforeEach
    void setUp() throws Exception {

        try (Connection connection =
                     postgres.createConnection("");
             Statement statement =
                     connection.createStatement()) {

            statement.executeUpdate(
                    "DELETE FROM transactions"
            );

            statement.executeUpdate(
                    "DELETE FROM accounts"
            );

            statement.executeUpdate(
                    "DELETE FROM users"
            );

            statement.executeUpdate(
                    "INSERT INTO users " +
                    "(id, name, email, password_hash) VALUES " +
                    "(1, 'Concurrent Sender', " +
                    "'sender@test.com', 'password'), " +
                    "(2, 'Receiver 1', " +
                    "'receiver1@test.com', 'password'), " +
                    "(3, 'Receiver 2', " +
                    "'receiver2@test.com', 'password')"
            );
        }
    }

    @Test
    void concurrentTransfersShouldNotAllowOverdraft()
            throws Exception {

        AccountRepository accountRepository =
                new PostgresAccountRepository();

        TransferRepository transferRepository =
                new PostgresTransferRepository();

        // Create sender with €100
        Account sender =
                accountRepository.save(
                        new Account(
                                0,
                                1,
                                100.00,
                                "1111"
                        )
                );

        // Create two receivers
        Account receiver1 =
                accountRepository.save(
                        new Account(
                                0,
                                2,
                                0.00,
                                "2222"
                        )
                );

        Account receiver2 =
                accountRepository.save(
                        new Account(
                                0,
                                3,
                                0.00,
                                "3333"
                        )
                );

        CountDownLatch startSignal =
                new CountDownLatch(1);

        ExecutorService executor =
                Executors.newFixedThreadPool(2);

        Future<Boolean> transfer1 =
                executor.submit(() -> {

                    startSignal.await();

                    try {

                        transferRepository.transfer(
                                sender.getId(),
                                receiver1.getId(),
                                80.00,
                                "Concurrent transfer 1"
                        );

                        return true;

                    } catch (Exception e) {

                        return false;
                    }
                });

        Future<Boolean> transfer2 =
                executor.submit(() -> {

                    startSignal.await();

                    try {

                        transferRepository.transfer(
                                sender.getId(),
                                receiver2.getId(),
                                80.00,
                                "Concurrent transfer 2"
                        );

                        return true;

                    } catch (Exception e) {

                        return false;
                    }
                });

        // Release both threads
        startSignal.countDown();

        boolean result1 = transfer1.get();
        boolean result2 = transfer2.get();

        executor.shutdown();

        assertTrue(
                result1 ^ result2,
                "Exactly one concurrent transfer should succeed"
        );

        Account finalSender =
                accountRepository.findById(
                        sender.getId()
                );

        assertNotNull(finalSender);

        assertEquals(
                20.00,
                finalSender.getBalance(),
                0.001
        );

        Account finalReceiver1 =
                accountRepository.findById(
                        receiver1.getId()
                );

        Account finalReceiver2 =
                accountRepository.findById(
                        receiver2.getId()
                );

        assertNotNull(finalReceiver1);
        assertNotNull(finalReceiver2);

        assertTrue(
                (
                    Math.abs(
                        finalReceiver1.getBalance() - 80.00
                    ) < 0.001
                    &&
                    Math.abs(
                        finalReceiver2.getBalance()
                    ) < 0.001
                )
                ||
                (
                    Math.abs(
                        finalReceiver1.getBalance()
                    ) < 0.001
                    &&
                    Math.abs(
                        finalReceiver2.getBalance() - 80.00
                    ) < 0.001
                )
        );
    }
}

