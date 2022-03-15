CREATE TABLE balance (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    amount BIGINT DEFAULT 0 NOT NULL,
    account_id int NOT NULL,
    currency_id BIGSERIAL NOT NULL,
    CONSTRAINT fk_balance_account
                     FOREIGN KEY (account_id)
                     REFERENCES account (id),
    CONSTRAINT fk_balance_currency
        FOREIGN KEY (currency_id)
            REFERENCES currency (id)
);

INSERT INTO balance (amount, account_id, currency_id)
VALUES
       (6700, 1, 1),
       (40000, 1, 3),
       (124500, 2, 3),
       (5000, 3, 4),
       (3400, 3, 2),
       (31700, 4, 2)
       ;