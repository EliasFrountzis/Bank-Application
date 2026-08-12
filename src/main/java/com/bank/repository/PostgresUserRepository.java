package com.bank.repository;

import com.bank.database.DatabaseConnection;
import com.bank.model.User;
import com.bank.jooq.tables.records.UsersRecord;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static com.bank.jooq.tables.Users.USERS;

public class PostgresUserRepository implements UserRepository {

    private DSLContext getDsl() {

        try {
            Connection connection =
                    DatabaseConnection.getConnection();

            return DSL.using(connection);
            
        } catch (SQLException e) {

            throw new RuntimeException(
                    "Could not connect to database",
                    e
            );
        }
    }

    @Override
    public User save(User user) {

        DSLContext dsl = getDsl();

        UsersRecord record =
                dsl.insertInto(USERS)
                        .set(USERS.NAME, user.getName())
                        .set(USERS.EMAIL, user.getEmail())
                        .set(USERS.PASSWORD_HASH, user.getPasswordHash())
                        .returning()
                        .fetchOne();

        if (record == null) {
            throw new RuntimeException(
                    "Failed to save user"
            );
        }

        return new User(
                record.getId(),
                record.getName(),
                record.getEmail(),
                record.getPasswordHash()
        );
    }

    @Override
    public User findById(int id) {

        DSLContext dsl = getDsl();

        UsersRecord record =
                dsl.selectFrom(USERS)
                        .where(USERS.ID.eq(id))
                        .fetchOne();

        if (record == null) {
            return null;
        }

        return new User(
                record.getId(),
                record.getName(),
                record.getEmail(),
                record.getPasswordHash()
        );
    }

    @Override
    public User findByEmail(String email) {

        DSLContext dsl = getDsl();

        UsersRecord record =
                dsl.selectFrom(USERS)
                        .where(USERS.EMAIL.eq(email))
                        .fetchOne();

        if (record == null) {
            return null;
        }

        return new User(
                record.getId(),
                record.getName(),
                record.getEmail(),
                record.getPasswordHash()
        );
    }

    @Override
    public List<User> findAll() {

        DSLContext dsl = getDsl();

        return dsl.selectFrom(USERS)
                .fetch()
                .map(record ->
                        new User(
                                record.getId(),
                                record.getName(),
                                record.getEmail(),
                                record.getPasswordHash()
                        )
                );
    }
}

