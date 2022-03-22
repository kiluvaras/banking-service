CREATE table account(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    created_at timestamp default now(),
    updated_at timestamp default now(),
    customer_id BIGSERIAL NOT NULL,
    country VARCHAR
);

CREATE TABLE currency(
                         id BIGSERIAL NOT NULL PRIMARY KEY,
                         created_at timestamp default now(),
                         updated_at timestamp default now(),
                         name TEXT,
                         iso_code VARCHAR(3) NOT NULL
);

CREATE TABLE balance (
                         id BIGSERIAL NOT NULL PRIMARY KEY,
                         created_at timestamp default now(),
                         updated_at timestamp default now(),
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

CREATE TYPE direction AS ENUM ('IN', 'OUT');

CREATE TABLE transaction(
                            id bigserial NOT NULL PRIMARY KEY,
                            created_at timestamp default now(),
                            updated_at timestamp default now(),
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

CREATE OR REPLACE FUNCTION update_timestamp()
    RETURNS TRIGGER language 'plpgsql' AS
$$
BEGIN
    NEW.updated_at = now();
    RETURN NEW;
END;
$$;

CREATE TRIGGER t_account_updated_at
    BEFORE UPDATE
    ON account
    FOR EACH ROW
EXECUTE PROCEDURE update_timestamp();

CREATE TRIGGER t_balance_updated_at
    BEFORE UPDATE
    ON balance
    FOR EACH ROW
EXECUTE PROCEDURE update_timestamp();

CREATE TRIGGER t_currency_updated_at
    BEFORE UPDATE
    ON currency
    FOR EACH ROW
EXECUTE PROCEDURE update_timestamp();

CREATE TRIGGER t_transaction_updated_at
    BEFORE UPDATE
    ON transaction
    FOR EACH ROW
EXECUTE PROCEDURE update_timestamp();