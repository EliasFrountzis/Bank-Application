package com.bank.repository;

import com.bank.database.DatabaseConnection;
import com.bank.exception.BankException;
import com.bank.jooq.tables.records.AccountsRecord;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDateTime;

import static com.bank.jooq.tables.Accounts.ACCOUNTS;
import static com.bank.jooq.tables.Transactions.TRANSACTIONS;

public class PostgresTransferRepository implements TransferRepository {

    @Override
    public void transfer(
            int fromAccount,
            int toAccount,
            double amount
    ) {

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {

                DSLContext dsl = DSL.using(connection);

                /*
                 * Lock both accounts in a consistent order.
                 *
                 * This prevents two opposite transfers from
                 * acquiring the locks in different orders.
                 */
                int firstLock = Math.min(fromAccount, toAccount);
                int secondLock = Math.max(fromAccount, toAccount);

                double senderBalance = 0;

                /*
                 * Lock the first account.
                 */
                AccountsRecord firstRecord =
                        dsl.selectFrom(ACCOUNTS)
                                .where(ACCOUNTS.ID.eq(firstLock))
                                .forUpdate()
                                .fetchOne();

                if (firstRecord == null) {

                    throw new BankException(
                            "Account with id " + firstLock + " not found",
                            404
                    );
                }

                if (firstLock == fromAccount) {
                    senderBalance =
                            firstRecord.get(ACCOUNTS.BALANCE).doubleValue();
                }

                /*
                 * Lock the second account.
                 */
                AccountsRecord secondRecord =
                        dsl.selectFrom(ACCOUNTS)
                                .where(ACCOUNTS.ID.eq(secondLock))
                                .forUpdate()
                                .fetchOne();

                if (secondRecord == null) {

                    throw new BankException(
                            "Account with id " + secondLock + " not found",
                            404
                    );
                }

                if (secondLock == fromAccount) {
                    senderBalance =
                            secondRecord.get(ACCOUNTS.BALANCE).doubleValue();
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

                /*
                 * Withdraw from sender.
                 */
                dsl.update(ACCOUNTS)
                        .set(
                                ACCOUNTS.BALANCE,
                                ACCOUNTS.BALANCE.subtract(
                                        BigDecimal.valueOf(amount)
                                )
                        )
                        .where(ACCOUNTS.ID.eq(fromAccount))
                        .execute();

                /*
                 * Deposit into receiver.
                 */
                dsl.update(ACCOUNTS)
                        .set(
                                ACCOUNTS.BALANCE,
                                ACCOUNTS.BALANCE.add(
                                        BigDecimal.valueOf(amount)
                                )
                        )
                        .where(ACCOUNTS.ID.eq(toAccount))
                        .execute();

                /*
                 * Record the transaction.
                 */
                dsl.insertInto(TRANSACTIONS)
                        .set(
                                TRANSACTIONS.FROM_ACCOUNT,
                                fromAccount
                        )
                        .set(
                                TRANSACTIONS.TO_ACCOUNT,
                                toAccount
                        )
                        .set(
                                TRANSACTIONS.AMOUNT,
                                BigDecimal.valueOf(amount)
                        )
                        .set(
                                TRANSACTIONS.TIMESTAMP,
                                LocalDateTime.now()
                        )
                        .execute();

                /*
                 * Everything succeeded.
                 */
                connection.commit();

            } catch (Exception e) {

                /*
                 * Any failure rolls back the entire transfer:
                 *
                 * - account withdrawal
                 * - account deposit
                 * - transaction record
                 */
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

