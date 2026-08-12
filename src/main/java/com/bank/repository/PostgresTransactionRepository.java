package com.bank.repository;

import com.bank.database.DatabaseConnection;
import com.bank.model.Transaction;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.bank.jooq.tables.Transactions.TRANSACTIONS;

public class PostgresTransactionRepository
        implements TransactionRepository {


    @Override
    public Transaction save(Transaction transaction) {

        try (java.sql.Connection connection =
                     DatabaseConnection.getConnection()) {

            DSLContext dsl = DSL.using(connection);

            var query =
                    dsl.insertInto(TRANSACTIONS)
                            .set(
                                    TRANSACTIONS.ACCOUNT_ID,
                                    transaction.getAccountId()
                            )
                            .set(
                                    TRANSACTIONS.TYPE,
                                    transaction.getType()
                            )
                            .set(
                                    TRANSACTIONS.FROM_ACCOUNT,
                                    transaction.getFromAccount()
                            )
                            .set(
                                    TRANSACTIONS.TO_ACCOUNT,
                                    transaction.getToAccount()
                            )
                            .set(
                                    TRANSACTIONS.AMOUNT,
                                    BigDecimal.valueOf(
                                            transaction.getAmount()
                                    )
                            )
                            .set(
                                    TRANSACTIONS.DESCRIPTION,
                                    transaction.getDescription()
                            )
                            .set(
                                    TRANSACTIONS.TIMESTAMP,
                                    LocalDateTime.parse(
                                            transaction.getTimestamp()
                                    )
                            );

            var record =
                    query.returning(TRANSACTIONS.ID)
                         .fetchOne();

            if (record == null) {
                return null;
            }

            return new Transaction(
                    record.get(TRANSACTIONS.ID),
                    transaction.getAccountId(),
                    transaction.getType(),
                    transaction.getFromAccount(),
                    transaction.getToAccount(),
                    transaction.getAmount(),
                    transaction.getDescription(),
                    transaction.getTimestamp()
            );

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Transaction> findAll() {

        try (java.sql.Connection connection =
                     DatabaseConnection.getConnection()) {

            DSLContext dsl = DSL.using(connection);

            return dsl.selectFrom(TRANSACTIONS)
                    .fetch()
                    .map(record ->
                            new Transaction(
                                    record.get(TRANSACTIONS.ID),
                                    record.get(TRANSACTIONS.ACCOUNT_ID),
                                    record.get(TRANSACTIONS.TYPE),
                                    record.get(TRANSACTIONS.FROM_ACCOUNT),
                                    record.get(TRANSACTIONS.TO_ACCOUNT),
                                    record.get(TRANSACTIONS.AMOUNT)
                                            .doubleValue(),
                                    record.get(
                                            TRANSACTIONS.DESCRIPTION
                                    ),
                                    record.get(
                                            TRANSACTIONS.TIMESTAMP
                                    ).toString()
                            )
                    );

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Transaction> findByAccountId(int accountId) {

        try (java.sql.Connection connection =
                     DatabaseConnection.getConnection()) {

            DSLContext dsl = DSL.using(connection);

            return dsl.selectFrom(TRANSACTIONS)
                    .where(
                            TRANSACTIONS.ACCOUNT_ID.eq(accountId)
                                    .or(
                                            TRANSACTIONS.FROM_ACCOUNT
                                                    .eq(accountId)
                                    )
                                    .or(
                                            TRANSACTIONS.TO_ACCOUNT
                                                    .eq(accountId)
                                    )
                    )
                    .fetch()
                    .map(record ->
                            new Transaction(
                                    record.get(TRANSACTIONS.ID),
                                    record.get(
                                            TRANSACTIONS.ACCOUNT_ID
                                    ),
                                    record.get(
                                            TRANSACTIONS.TYPE
                                    ),
                                    record.get(
                                            TRANSACTIONS.FROM_ACCOUNT
                                    ),
                                    record.get(
                                            TRANSACTIONS.TO_ACCOUNT
                                    ),
                                    record.get(
                                            TRANSACTIONS.AMOUNT
                                    ).doubleValue(),
                                    record.get(
                                            TRANSACTIONS.DESCRIPTION
                                    ),
                                    record.get(
                                            TRANSACTIONS.TIMESTAMP
                                    ).toString()
                            )
                    );

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }
}

