package com.bank.repository;

import com.bank.database.PostgresIntegrationTest;
import com.bank.model.Account;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

public class PostgresTransferRepositoryTest extends PostgresIntegrationTest {

    @Test
    void concurrentTransfersShouldNotAllowOverdraft() throws Exception {

        AccountRepository accountRepository =
                new PostgresAccountRepository();

        TransferRepository transferRepository =
                new PostgresTransferRepository();

        // Create sender with €100
        Account sender =
                accountRepository.save(
                        new Account(0, "Concurrent Sender", 100.00)
                );

        // Create two receivers
        Account receiver1 =
                accountRepository.save(
                        new Account(0, "Receiver 1", 0.00)
                );

        Account receiver2 =
                accountRepository.save(
                        new Account(0, "Receiver 2", 0.00)
                );

        /*
         * Both transfers start at approximately the same time.
         *
         * Each tries to transfer €80 from the same €100 account.
         *
         * Only ONE should succeed.
         */
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
                                80.00
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
                                80.00
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

        /*
         * Exactly one transfer must succeed.
         */
        assertTrue(
                result1 ^ result2,
                "Exactly one concurrent transfer should succeed"
        );

        /*
         * Sender must have exactly €20 remaining.
         */
        Account finalSender =
                accountRepository.findById(sender.getId());

        assertEquals(
                20.00,
                finalSender.getBalance(),
                0.001
        );

        /*
         * Exactly one receiver should have €80.
         */
        Account finalReceiver1 =
                accountRepository.findById(receiver1.getId());

        Account finalReceiver2 =
                accountRepository.findById(receiver2.getId());

        assertTrue(
                (finalReceiver1.getBalance() == 80.00
                        && finalReceiver2.getBalance() == 0.00)
                ||
                (finalReceiver1.getBalance() == 0.00
                        && finalReceiver2.getBalance() == 80.00)
        );
    }
}