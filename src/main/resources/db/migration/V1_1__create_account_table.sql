CREATE table account(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    customer_id BIGSERIAL NOT NULL,
    country VARCHAR
);

INSERT INTO account(customer_id, country)
VALUES (1, 'Estonia'),
       (2, 'Sweden'),
       (3, 'USA'),
       (4, 'UK');