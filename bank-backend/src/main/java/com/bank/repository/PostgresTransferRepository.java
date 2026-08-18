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
        double amount,
        String description
) {

        if (amount <= 0) {
            throw new BankException(
                    "Transfer amount must be greater than zero",
                    400
            );
        }

        if (fromAccount == toAccount) {
            throw new BankException(
                    "Cannot transfer money to the same account",
                    400
            );
        }

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {

                DSLContext dsl = DSL.using(connection);

              
                int firstLock = Math.min(fromAccount, toAccount);
                int secondLock = Math.max(fromAccount, toAccount);

                double senderBalance = 0;

                
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

              
                if (senderBalance < amount) {
                    throw new BankException(
                            "Insufficient funds",
                            400
                    );
                }

               
                dsl.update(ACCOUNTS)
                        .set(
                                ACCOUNTS.BALANCE,
                                ACCOUNTS.BALANCE.subtract(
                                        BigDecimal.valueOf(amount)
                                )
                        )
                        .where(ACCOUNTS.ID.eq(fromAccount))
                        .execute();

              
                dsl.update(ACCOUNTS)
                        .set(
                                ACCOUNTS.BALANCE,
                                ACCOUNTS.BALANCE.add(
                                        BigDecimal.valueOf(amount)
                                )
                        )
                        .where(ACCOUNTS.ID.eq(toAccount))
                        .execute();

                
               dsl.insertInto(TRANSACTIONS)
    .set(
            TRANSACTIONS.ACCOUNT_ID,
            (Integer) null
    )
    .set(
            TRANSACTIONS.TYPE,
            "TRANSFER"
    )
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
            TRANSACTIONS.DESCRIPTION,
            description
    )
    .set(
            TRANSACTIONS.TIMESTAMP,
            LocalDateTime.now()
    )
    .execute();
                
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