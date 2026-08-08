# Banking Backend

A small banking backend built with Java. The project provides a simple API for creating accounts, transferring money, and viewing transaction history.

I built this project to get more practical experience with Java backend development and to work with technologies such as PostgreSQL, jOOQ, Docker, automated testing, and database transactions.



## Features

    * Create bank accounts
    * View all accounts
    * View an account by ID
    * Transfer money between accounts
    * Validate account creation and transfers
    * Prevent transfers with insufficient funds
    * Prevent transfers between the same account
    * Store transaction history
    * View all transactions
    * View transactions for a specific account
    * PostgreSQL persistence
    * Atomic money transfers with rollback
    * Row-level locking for concurrent transfers
    * Unit, controller, and PostgreSQL integration tests
    * jOOQ-generated database classes



## Technologies

    * **Java 21**
    * **Maven** – project and dependency management
    * **SparkJava** – HTTP/REST API
    * **Gson** – JSON handling
    * **PostgreSQL** – database
    * **jOOQ** – type-safe SQL and database code generation
    * **JUnit 5** – testing
    * **Testcontainers** – PostgreSQL integration testing
    * **Docker** – running PostgreSQL locally



# Running the Application

## Requirements

You will need:

* Java 21
* Maven
* Docker Desktop

PostgreSQL is run through Docker

### 1. Start PostgreSQL

From the project directory:

Command line:
  docker compose up -d


### 2. Run the application

Command line:
  mvn exec:java


The SparkJava server will start on:
```text
http://localhost:4567
```




# Testing


1. Unit Tests

The service layer is tested using in-memory repositories. This keeps the tests independent from PostgreSQL and focuses them on the application's business logic.

AccountServiceTest — 4 tests

  Tests include:

    Creating an account with valid information
    Rejecting an empty owner
    Rejecting a negative balance
    Finding an existing account
    TransactionServiceTest — 2 tests

  Tests include:

    Creating a transaction
    Retrieving all transactions and checking the returned transaction data
    TransferServiceTest — 3 tests

  Tests include:

    Successfully transferring money between two accounts
    Rejecting a transfer when the sender has insufficient funds
    Rejecting a transfer when an account does not exist

Unit tests: 9

2. Controller Tests

The controller tests start the SparkJava server and send actual HTTP requests to the API.

These tests use the in-memory repositories, so they focus on whether the HTTP layer correctly communicates with the service layer and returns the expected status codes and data.

AccountControllerTest — 5 tests

  Tests include:

    Creating an account through HTTP
    Getting all accounts
    Getting an account by ID
    Rejecting invalid account creation
    Returning 404 when an account does not exist
    TransactionControllerTest — 2 tests

  Tests include:

    Getting all transactions through HTTP
    Getting transactions for a specific account
    Checking returned transaction data
    TransferControllerTest — 1 test

  Tests include:

    Performing a money transfer through HTTP
    Checking that a successful transfer returns 200 OK

Controller tests: 8


## Running the Tests

Run the complete test suite with:

Command line:
  mvn clean test





# Error Handling

The application uses a custom `BankException` for business-related errors.

Some examples are:

* Invalid account information
* Account not found
* Invalid transfer amount
* Insufficient funds
* Attempting to transfer money to the same account

The `ExceptionHandler` converts these exceptions into HTTP responses.





# API

## Accounts

### Create an account

```http
POST /accounts
```

Example request:

```json
{
    "owner": "Alice",
    "balance": 1000
}
```

Successful response:

```text
201 Created
```

### Get all accounts

```http
GET /accounts
```

Returns the accounts currently stored in the database.

### Get an account by ID

```http
GET /accounts/{id}
```

Example:

```http
GET /accounts/1
```

Returns `404 Not Found` if the account does not exist.

---

# Transfers

### Transfer money

```http
POST /transfer
```

Example request:

```json
{
    "fromAccount": 1,
    "toAccount": 2,
    "amount": 100
}
```

Successful response:

```text
200 OK
```

A transfer is handled as a single PostgreSQL transaction.

  The application:

    1. Locks the two accounts.
    2. Checks that they exist.
    3. Checks that the sender has enough money.
    4. Withdraws the amount from the sender.
    5. Adds the amount to the receiver.
    6. Creates a transaction record.
    7. Commits everything together.

If something goes wrong, the database transaction is rolled back.

The accounts are locked in a consistent order to reduce the risk of deadlocks when transfers happen concurrently.



# Transactions

### Get all transactions

```http
GET /transactions
```

Returns the transaction history.

### Get transactions for an account

```http
GET /accounts/{id}/transactions
```

Returns transactions where the account was either the sender or the receiver.

Transaction timestamps are stored in PostgreSQL and read back from the database, so the original transaction time is preserved.

---

# Database

The project uses PostgreSQL for persistent storage.

The main tables are:

```text
accounts
---------
id
owner
balance


transactions
------------
id
from_account
to_account
amount
timestamp
```

The database schema is located at:

```text
src/main/resources/schema.sql
```

PostgreSQL can be started locally using Docker Compose.



# jOOQ

jOOQ is used to generate Java classes from the PostgreSQL database schema.

This allows the repositories to use generated table and field definitions instead of writing all database queries as raw SQL.

jOOQ generation is configured in `pom.xml`.





# Future Improvements

Some things I would like to add in a future version are:

* Authentication and authorization
* User accounts and login
* Better security and password management
* More detailed API responses
* Pagination for transaction history
* Currency support
* Dockerizing the whole application
* Connecting the backend to an Android/iOS client
* Cloud deployment
* Redis caching
* Kubernetes deployment

