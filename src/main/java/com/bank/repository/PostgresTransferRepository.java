package com.bank.repository;

import com.bank.database.DatabaseConnection;
import com.bank.exception.BankException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class PostgresTransferRepository implements TransferRepository {

    @Override
    public void transfer(
            int fromAccount,
            int toAccount,
            double amount
    ) {

        String lockSql =
                """
                SELECT id, balance
                FROM accounts
                WHERE id = ?
                FOR UPDATE
                """;

        String withdrawSql =
                """
                UPDATE accounts
                SET balance = balance - ?
                WHERE id = ?
                """;

        String depositSql =
                """
                UPDATE accounts
                SET balance = balance + ?
                WHERE id = ?
                """;

        String transactionSql =
                """
                INSERT INTO transactions
                (from_account, to_account, amount, timestamp)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {

                /*
                 * Lock both accounts in a consistent order.
                 * This prevents two opposite transfers from
                 * acquiring the locks in different orders.
                 */
                int firstLock = Math.min(fromAccount, toAccount);
                int secondLock = Math.max(fromAccount, toAccount);

                double senderBalance = 0;

                try (PreparedStatement statement =
                             connection.prepareStatement(lockSql)) {

                    statement.setInt(1, firstLock);

                    try (ResultSet result = statement.executeQuery()) {

                        if (!result.next()) {
                            throw new BankException(
                                    "Account with id " + firstLock + " not found",
                                    404
                            );
                        }

                        if (firstLock == fromAccount) {
                            senderBalance = result.getDouble("balance");
                        }
                    }
                }

                try (PreparedStatement statement =
                             connection.prepareStatement(lockSql)) {

                    statement.setInt(1, secondLock);

                    try (ResultSet result = statement.executeQuery()) {

                        if (!result.next()) {
                            throw new BankException(
                                    "Account with id " + secondLock + " not found",
                                    404
                            );
                        }

                        if (secondLock == fromAccount) {
                            senderBalance = result.getDouble("balance");
                        }
                    }
                }

                /*
                 * The balance check happens AFTER the sender row
                 * has been locked.
                 *
                 * Therefore another concurrent transfer cannot
                 * change this balance while we are using it.
                 */
                if (senderBalance < amount) {

                    throw new BankException(
                            "Insufficient funds",
                            400
                    );
                }

                try (PreparedStatement withdraw =
                             connection.prepareStatement(withdrawSql)) {

                    withdraw.setDouble(1, amount);
                    withdraw.setInt(2, fromAccount);

                    withdraw.executeUpdate();
                }

                try (PreparedStatement deposit =
                             connection.prepareStatement(depositSql)) {

                    deposit.setDouble(1, amount);
                    deposit.setInt(2, toAccount);

                    deposit.executeUpdate();
                }

                try (PreparedStatement transaction =
                             connection.prepareStatement(transactionSql)) {

                    transaction.setInt(1, fromAccount);
                    transaction.setInt(2, toAccount);
                    transaction.setDouble(3, amount);
                    transaction.setTimestamp(
                            4,
                            Timestamp.valueOf(LocalDateTime.now())
                    );

                    transaction.executeUpdate();
                }

                connection.commit();

            } catch (Exception e) {

                connection.rollback();
                throw e;
            }

        } catch (BankException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Transfer failed",
                    e
            );
        }
    }
}