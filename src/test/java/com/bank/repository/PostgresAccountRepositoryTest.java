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
        }
    }

    @Test
    void shouldSaveAndFindAccount() {

        Account account = new Account(
                0,
                "Ilias",
                1000.00
        );

        Account saved = repository.save(account);

        assertNotNull(saved);
        assertTrue(saved.getId() > 0);
        assertEquals("Ilias", saved.getOwner());
        assertEquals(1000.00, saved.getBalance());

        Account found = repository.findById(saved.getId());

        assertNotNull(found);
        assertEquals(saved.getId(), found.getId());
        assertEquals("Ilias", found.getOwner());
        assertEquals(1000.00, found.getBalance());
    }

    @Test
    void shouldFindAllAccounts() {

        repository.save(
                new Account(0, "Ilias", 1000.00)
        );

        repository.save(
                new Account(0, "John", 500.00)
        );

        var accounts = repository.findAll();

        assertEquals(2, accounts.size());
    }

    @Test
    void shouldUpdateAccount() {

        Account saved = repository.save(
                new Account(0, "Ilias", 1000.00)
        );

        Account updated = new Account(
                saved.getId(),
                "Ilias Updated",
                2000.00
        );

        repository.update(updated);

        Account found = repository.findById(saved.getId());

        assertNotNull(found);
        assertEquals("Ilias Updated", found.getOwner());
        assertEquals(2000.00, found.getBalance());
    }
}