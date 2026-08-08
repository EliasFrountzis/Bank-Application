package com.bank.database;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class PostgresIntegrationTest {

    @Container
    protected static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16")
                    .withDatabaseName("bankdb")
                    .withUsername("postgres")
                    .withPassword("1234")
                    .withInitScript("schema.sql");

    @BeforeAll
    static void startContainer() {

        postgres.start();

        System.setProperty(
                "db.url",
                postgres.getJdbcUrl()
        );

        System.setProperty(
                "db.username",
                postgres.getUsername()
        );

        System.setProperty(
                "db.password",
                postgres.getPassword()
        );
    }
    @AfterAll
static void stopContainer() {
    postgres.stop();
}
}