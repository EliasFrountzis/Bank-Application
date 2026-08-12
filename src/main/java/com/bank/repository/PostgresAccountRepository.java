package com.bank.repository;

import com.bank.database.DatabaseConnection;
import com.bank.jooq.tables.records.AccountsRecord;
import com.bank.model.Account;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

import static com.bank.jooq.tables.Accounts.ACCOUNTS;

public class PostgresAccountRepository implements AccountRepository {

    @Override
    public Account save(Account account) {

        try (Connection connection = DatabaseConnection.getConnection()) {

            DSLContext dsl = DSL.using(connection);

            AccountsRecord record =
                    dsl.insertInto(ACCOUNTS)
                            .set(ACCOUNTS.USER_ID, account.getUserId())
                            .set(ACCOUNTS.BALANCE,
                                    BigDecimal.valueOf(account.getBalance()))
                            .set(ACCOUNTS.CARD_LAST4, account.getCardLast4())
                            .returning(
                                    ACCOUNTS.ID,
                                    ACCOUNTS.USER_ID,
                                    ACCOUNTS.BALANCE,
                                    ACCOUNTS.CARD_LAST4
                            )
                            .fetchOne();

            return new Account(
                    record.get(ACCOUNTS.ID),
                    record.get(ACCOUNTS.USER_ID),
                    record.get(ACCOUNTS.BALANCE).doubleValue(),
                    record.get(ACCOUNTS.CARD_LAST4)
            );

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Account> findAll() {

        try (Connection connection = DatabaseConnection.getConnection()) {

            DSLContext dsl = DSL.using(connection);

            return dsl.selectFrom(ACCOUNTS)
                    .fetch()
                    .map(record ->
                            new Account(
                                    record.get(ACCOUNTS.ID),
                                    record.get(ACCOUNTS.USER_ID),
                                    record.get(ACCOUNTS.BALANCE).doubleValue(),
                                    record.get(ACCOUNTS.CARD_LAST4)
                            )
                    );

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Account findById(int id) {

        try (Connection connection = DatabaseConnection.getConnection()) {

            DSLContext dsl = DSL.using(connection);

            AccountsRecord record =
                    dsl.selectFrom(ACCOUNTS)
                            .where(ACCOUNTS.ID.eq(id))
                            .fetchOne();

            if (record == null) {
                return null;
            }

            return new Account(
                    record.get(ACCOUNTS.ID),
                    record.get(ACCOUNTS.USER_ID),
                    record.get(ACCOUNTS.BALANCE).doubleValue(),
                    record.get(ACCOUNTS.CARD_LAST4)
            );

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Account account) {

        try (Connection connection = DatabaseConnection.getConnection()) {

            DSLContext dsl = DSL.using(connection);

            dsl.update(ACCOUNTS)
                    .set(ACCOUNTS.USER_ID, account.getUserId())
                    .set(
                            ACCOUNTS.BALANCE,
                            BigDecimal.valueOf(account.getBalance())
                    )
                    .set(ACCOUNTS.CARD_LAST4, account.getCardLast4())
                    .where(ACCOUNTS.ID.eq(account.getId()))
                    .execute();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}