package com.bank.repository;

import com.bank.database.PostgresIntegrationTest;
import com.bank.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class PostgresUserRepositoryTest extends PostgresIntegrationTest {

    private PostgresUserRepository repository;

    @BeforeEach
    void setUp() throws Exception {

        repository = new PostgresUserRepository();

        try (Connection connection = postgres.createConnection("");
             Statement statement = connection.createStatement()) {

            statement.executeUpdate("DELETE FROM transactions");
            statement.executeUpdate("DELETE FROM accounts");
            statement.executeUpdate("DELETE FROM users");
        }
    }

    @Test
    void shouldSaveAndFindUser() {

        User user =
                new User(
                        0,
                        "Ilias",
                        "ilias@test.com",
                        "password-hash"
                );

        User saved =
                repository.save(user);

        assertNotNull(saved);
        assertTrue(saved.getId() > 0);

        assertEquals(
                "Ilias",
                saved.getName()
        );

        assertEquals(
                "ilias@test.com",
                saved.getEmail()
        );

        assertEquals(
                "password-hash",
                saved.getPasswordHash()
        );

        User found =
                repository.findById(saved.getId());

        assertNotNull(found);

        assertEquals(
                saved.getId(),
                found.getId()
        );

        assertEquals(
                "Ilias",
                found.getName()
        );

        assertEquals(
                "ilias@test.com",
                found.getEmail()
        );

        assertEquals(
                "password-hash",
                found.getPasswordHash()
        );
    }

    @Test
    void shouldFindUserByEmail() {

        User saved =
                repository.save(
                        new User(
                                0,
                                "Ilias",
                                "ilias@test.com",
                                "password-hash"
                        )
                );

        User found =
                repository.findByEmail(
                        "ilias@test.com"
                );

        assertNotNull(found);

        assertEquals(
                saved.getId(),
                found.getId()
        );

        assertEquals(
                "Ilias",
                found.getName()
        );

        assertEquals(
                "ilias@test.com",
                found.getEmail()
        );
    }

    @Test
    void shouldReturnNullForNonexistentUser() {

        User found =
                repository.findById(999);

        assertNull(found);
    }

    @Test
    void shouldReturnNullForNonexistentEmail() {

        User found =
                repository.findByEmail(
                        "doesnotexist@test.com"
                );

        assertNull(found);
    }

    @Test
    void shouldFindAllUsers() {

        repository.save(
                new User(
                        0,
                        "Ilias",
                        "ilias@test.com",
                        "password-hash"
                )
        );

        repository.save(
                new User(
                        0,
                        "John",
                        "john@test.com",
                        "password-hash"
                )
        );

        var users =
                repository.findAll();

        assertEquals(
                2,
                users.size()
        );
    }
}