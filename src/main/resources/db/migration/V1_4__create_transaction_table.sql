CREATE TYPE direction AS ENUM ('IN', 'OUT');

CREATE TABLE transaction(
    id bigserial NOT NULL PRIMARY KEY,
    direction direction NOT NULL,
    amount bigint NOT NULL,
    description text,
    account_id bigint,
    currency_id bigint,
    CONSTRAINT fk_transaction_account
        FOREIGN KEY (account_id)
            REFERENCES account (id),
    CONSTRAINT fk_transaction_currency
        FOREIGN KEY (currency_id)
            REFERENCES currency (id)
);

INSERT INTO transaction(direction, amount, description, currency_id, account_id)
VALUES ('IN', 3000, 'Birthday money', 1, 1);