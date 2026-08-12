package com.bank.repository;

import com.bank.database.PostgresIntegrationTest;
import com.bank.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class PostgresAccountRepositoryTest extends PostgresIntegrationTest {

    private PostgresAccountRepository repository;

    @BeforeEach
void setUp() throws Exception {

    repository = new PostgresAccountRepository();

    try (Connection connection = postgres.createConnection("");
         Statement statement = connection.createStatement()) {

        statement.executeUpdate("DELETE FROM transactions");
        statement.executeUpdate("DELETE FROM accounts");
        statement.executeUpdate("DELETE FROM users");

        statement.executeUpdate(
                "INSERT INTO users (id, name, email, password_hash) VALUES " +
                "(1, 'Ilias', 'ilias@test.com', 'test-hash-1'), " +
                "(2, 'John', 'john@test.com', 'test-hash-2')"
        );
    }
}

    @Test
    void shouldSaveAndFindAccount() {

        Account account =
                new Account(0, 1, 1000.00, "1234");

        Account saved = repository.save(account);

        assertNotNull(saved);
        assertTrue(saved.getId() > 0);
        assertEquals(1, saved.getUserId());
        assertEquals(1000.00, saved.getBalance());
        assertEquals("1234", saved.getCardLast4());

        Account found =
                repository.findById(saved.getId());

        assertNotNull(found);
        assertEquals(saved.getId(), found.getId());
        assertEquals(1, found.getUserId());
        assertEquals(1000.00, found.getBalance());
        assertEquals("1234", found.getCardLast4());
    }

    @Test
    void shouldFindAllAccounts() {

        repository.save(
                new Account(0, 1, 1000.00, "1234")
        );

        repository.save(
                new Account(0, 2, 500.00, "5678")
        );

        var accounts = repository.findAll();

        assertEquals(2, accounts.size());
    }

    @Test
    void shouldUpdateAccount() {

        Account saved =
                repository.save(
                        new Account(0, 1, 1000.00, "1234")
                );

        Account updated =
                new Account(
                        saved.getId(),
                        1,
                        2000.00,
                        "9999"
                );

        repository.update(updated);

        Account found =
                repository.findById(saved.getId());

        assertNotNull(found);
        assertEquals(1, found.getUserId());
        assertEquals(2000.00, found.getBalance());
        assertEquals("9999", found.getCardLast4());
    }
}

