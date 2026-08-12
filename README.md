# Banking Application

A full-stack banking application built as a way to learn new tools and practices as well as familiarize myself more with ones Ive used in the past.
The backend is built with Java and PostgreSQL, with jOOQ used for type-safe database access and Docker used to run the database locally.

### Backend

    Java 21
    Spark Java
    PostgreSQL 16
    jOOQ
    Maven
    Gson
    Docker / Docker Compose

### Testing

    JUnit 5
    Testcontainers
    In-memory repositories for unit testing
    PostgreSQL integration tests


### Frontend

The frontend is currently being developed separately using:

    React
    TypeScript
    Webpack


## Features

The backend currently supports:

    User creation
    User lookup
    Account creation
    Account lookup
    Account listing
    Deposits
    Withdrawals
    Transfers
    Transaction creation
    Transaction history
    Account-specific transaction history
    Input validation
    HTTP error handling
    PostgreSQL persistence
    Concurrent transfer protection
    Automated unit, integration, and controller tests


## Card Numbers

The application currently stores only the last four digits of a card number. Its important to note that i havent made the last digits unique as more that one cards can have the last 4 digits the same. More over making the last for digits unique would limit the ammount of accounts the application could have which although not a problem for this project, doesnt make sense for a banking app.

For example:
**** **** **** 1234


## API

### Users

#### Create user

POST /users

{
  "name": "Mark",
  "email": "mark@example.com",
  "password": "password123"
}


#### Get user

GET /users/:id




### Accounts

#### Create account

POST /accounts

{
  "userId": 1,
  "balance": 1000,
  "cardLast4": "1234"
}


#### Get all accounts

GET /accounts


#### Get account

GET /accounts/:id


#### Deposit

POST /accounts/:id/deposit


Example body:
100


#### Withdraw

POST /accounts/:id/withdraw


Example body:
50


### Transactions

#### Get all transactions

GET /transactions


#### Get transactions for an account

GET /accounts/:id/transactions



### Transfers

POST /transfers
{
  "fromAccount": 1,
  "toAccount": 2,
  "amount": 100,
  "description": "Groceries"
}




## Testing

The project contains multiple levels of automated testing.

### Unit Tests

Services are tested using in-memory repositories.

This allows business logic to be tested without requiring a running PostgreSQL database.


    UserServiceTest
    AccountServiceTest
    TransactionServiceTest
    TransferServiceTest

### Repository Integration Tests

PostgreSQL repositories are tested against a real PostgreSQL instance using Testcontainers.

 PostgresUserRepositoryTest
 PostgresAccountRepositoryTest
 PostgresTransactionRepositoryTest
 PostgresTransferRepositoryTest


### Controller Tests

HTTP endpoints are tested using Spark's embedded server and HTTP requests.

    UserControllerTest
    AccountControllerTest
    TransactionControllerTest
    TransferControllerTest

Run the complete test suite with:

mvn clean test



## Running the Backend

### 1. Start PostgreSQL

The project uses Docker Compose to run PostgreSQL.
Terminal:
    docker compose up -d


Verify that the database container is running:
Terminal:
    docker ps


### 2. Start the application

From the `bank-backend` directory:
Terminal:
    mvn clean compile


Then run the application using the project's configured main class.

The API will be available at:
http://localhost:4567


### 3. Run tests

Terminal:
    mvn clean test


## Future Improvements

Potential future improvements include:

* Password hashing and authentication
* Session or token-based authentication
* User-specific account access
* Improved transaction filtering
* Pagination
* Stronger validation
* More realistic card representation
* Frontend authentication
* Improved UI/UX
* Deployment
