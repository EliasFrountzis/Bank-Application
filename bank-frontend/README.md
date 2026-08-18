# SpBank Frontend

React + TypeScript frontend for the SpBank banking application.

The frontend provides a simple banking interface where users can log in, view their accounts, manage accounts, perform deposits and withdrawals, and transfer money between users.

## Features

- User login
- User registration
- Account dashboard
- View account balances
- View account details
- View recent transactions
- View all account transactions
- Deposit money
- Withdraw money
- Transfer money between users
- Search for recipients by name or email
- Select a recipient's active account
- Transfer confirmation screen
- Account settings
- Change account name
- Change account type
- Close an account
- Loading and error states
- Responsive layout for smaller screens

## Tech Stack

- React
- TypeScript
- HTML
- CSS
- Webpack
- npm

The frontend communicates with the SpBank backend through HTTP requests.

## Getting Started

The application consists of two parts:

- `bank-backend` — Java backend with SparkJava and PostgreSQL
- `bank-frontend` — React + TypeScript frontend

Both the backend and frontend need to be running at the same time.


## Prerequisites

Make sure you have installed:

- Java 21
- Maven
- Node.js
- npm
- Docker Desktop

The backend uses PostgreSQL through Docker.



## 1. Start the Database

Open a terminal in the backend directory:
cd bank-backend

Start PostgreSQL with Docker:
docker compose up -d


Start the backend:
mvn clean compile


## 2. Start the Frontend

Open a second terminal and navigate to the frontend:
cd bank-frontend

Install the dependencies:
npm.cmd install

Start the server:
npm.cmd start

The frontend will normally be available at:
http://localhost:3000


## Demo Accounts

Two test accounts are available for trying the application.
Email:    ilias@test.com
Password: test123

Email:    guillermo@test.com
Password: test123

Both accounts can be used to test the transfer functionality.

For example:

Log in as ilias@test.com.
Open one of Ilias' accounts.
Select Transfer Money.
Search for guillermo@test.com.
Select one of Guillermo's active accounts.
Enter an amount.
Enter a reason for the transfer.
Continue to the confirmation screen.
Review the transfer details.
Click Confirm Transfer.

You can then log out and log in as Guillermo to see the incoming transaction.


## Account Management

After logging in, you can:

View account balances
Open account details
View transactions
Deposit money
Withdraw money
Edit account settings
Close an account


## Transfers

Use the two demo users to test transfers between accounts.

The transfer process includes validation for:

Missing recipient
Recipient without an active account
Missing recipient account
Invalid amount
Insufficient funds
Missing transfer reason