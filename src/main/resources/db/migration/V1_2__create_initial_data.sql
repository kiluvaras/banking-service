INSERT INTO currency(name, iso_code)
VALUES ('Euro', 'EUR'),
       ('Pound sterling', 'GBP'),
       ('Swedish krona', 'SEK'),
       ('Unites States dollar', 'USD');

INSERT INTO account(customer_id, country)
VALUES (1, 'Estonia'),
       (2, 'Sweden'),
       (3, 'USA'),
       (4, 'UK');

INSERT INTO balance (amount, account_id, currency_id)
VALUES
    (6700, 1, 1),
    (40000, 1, 3),
    (124500, 2, 3),
    (5000, 3, 4),
    (3400, 3, 2),
    (31700, 4, 2);

INSERT INTO transaction(direction, amount, description, currency_id, account_id)
VALUES ('IN', 3000, 'Birthday money', 1, 1);