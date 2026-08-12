CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS accounts (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    balance NUMERIC(15,2) NOT NULL DEFAULT 0,
    card_last4 VARCHAR(4) NOT NULL,

    CONSTRAINT fk_account_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS transactions (
    id SERIAL PRIMARY KEY,

    account_id INTEGER,

    type VARCHAR(20) NOT NULL,

    from_account INTEGER,
    to_account INTEGER,

    amount NUMERIC(15,2) NOT NULL,

    description VARCHAR(255),

    timestamp TIMESTAMP NOT NULL,

    CONSTRAINT fk_transaction_account
        FOREIGN KEY (account_id)
        REFERENCES accounts(id),

    CONSTRAINT fk_from_account
        FOREIGN KEY (from_account)
        REFERENCES accounts(id),

    CONSTRAINT fk_to_account
        FOREIGN KEY (to_account)
        REFERENCES accounts(id),

    CONSTRAINT chk_transaction_type
        CHECK (type IN ('TRANSFER', 'DEPOSIT', 'WITHDRAWAL'))
);