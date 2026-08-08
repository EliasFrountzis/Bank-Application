/**Bank Backend

A Bank Backend built with Java and postgresSQL


/** Technologies used
1.Java 21
2.SparkJava
3.PostgreSQL
4.jOOQ
5.JUnit5
6.Maven
7.Docker
8.Testcontainers


/** Features
-Create and retrieve bank accounts
-Transfer money from one account to another
-Transaction history
-Concurrent transfer protection
-Automated tests
-jOOQ- generated database access


/** Running the application
To run the application apply the following steps
    1.Start PostgresSQl
        The project uses PostgresSQL through Docker
        so form the project root run the command:
            docker compose up -d

    2.Build the project
        To build the project run in the command line:
            mvn clean package
        This will compile the application, run the test suit and generate the jOOQ classes from the shema


    3.Start the application
        Run the comman:
            mvn exec:java
        this will locally start the server, The API can then be accessed at:http://localhost:4567


    4.Use Postman to make the API requests


    5.Stop PostgreSQL
        When finished, stop the database with:
            docker compose down



/**Running the In built tests
To run the complete test suite in the command line:
    mvn clean test

The project currently contains 21 automated tests covering the controllers, services, and PostgreSQL repositories.

/**## The Tests

A. Unit Tests

The service layer is tested using in-memory repositories. These tests focus on the application's business logic without requiring a database.

    1.`AccountServiceTest`

        * Creating an account with a valid owner and balance
        * Rejecting an empty owner
        * Rejecting a negative initial balance
        * Retrieving an existing account

    2.`TransactionServiceTest`

        * Creating a valid transaction
        * Retrieving all transactions
        * Verifying transaction data such as sender, receiver, and amount

    3.`TransferServiceTest`

        * Successfully transferring money between accounts
        * Rejecting transfers when the sender has insufficient funds
        * Rejecting transfers when an account does not exist
        * Verifying that account balances remain unchanged when a transfer fails

These tests verify that the core business logic and validation rules are enforced before interacting with the database.



B. Controller Tests

The controller tests verify the HTTP API exposed by the application. They use actual HTTP requests against the SparkJava server.

    1.`AccountControllerTest`

        * Creating accounts through HTTP requests
        * Retrieving all accounts
        * Retrieving an account by ID
        * Rejecting invalid account creation requests
        * Returning `404 Not Found` when an account does not exist
        * Verifying appropriate HTTP status codes

    2.`TransactionControllerTest`

        * Retrieving all transactions through HTTP requests
        * Retrieving transactions belonging to a specific account
        * Verifying that returned transaction data is present in the HTTP response

    3.`TransferControllerTest`

        * Performing a transfer through an HTTP request
        * Verifying a successful transfer response
        * Verifying the API endpoint is correctly connected to the transfer service

These tests verify that the HTTP layer correctly communicates with the service layer and returns the expected HTTP responses.



C. PostgreSQL Integration Tests

The project also contains integration tests using Testcontainers.

`PostgresIntegrationTest' starts a temporary PostgreSQL 16 database inside a Docker container for the integration tests.

This allows the application to test its actual PostgreSQL repositories against a real PostgreSQL database rather than using a mock database.

The integration tests verify that:

    * Accounts can be stored and retrieved from PostgreSQL
    * Transactions can be stored and retrieved from PostgreSQL
    * Database queries execute correctly
    * Java objects are correctly mapped to PostgreSQL data
    * Database constraints and relationships behave correctly
    * PostgreSQL transactions correctly commit or roll back when necessary

The database schema is automatically initialized from 'schema.sql' when the test container starts.


