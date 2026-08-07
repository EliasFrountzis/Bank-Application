CREATE TABLE IF NOT EXISTS accounts (
    id SERIAL PRIMARY KEY,
    owner VARCHAR(100) NOT NULL,
    balance NUMERIC(15,2) NOT NULL
);


CREATE TABLE IF NOT EXISTS transactions (
    id SERIAL PRIMARY KEY,
    from_account INTEGER NOT NULL,
    to_account INTEGER NOT NULL,
    amount NUMERIC(15,2) NOT NULL,
    timestamp TIMESTAMP NOT NULL,

    CONSTRAINT fk_from_account
        FOREIGN KEY (from_account)
        REFERENCES accounts(id),

    CONSTRAINT fk_to_account
        FOREIGN KEY (to_account)
        REFERENCES accounts(id)
);