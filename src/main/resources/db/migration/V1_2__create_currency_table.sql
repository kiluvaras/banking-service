CREATE TABLE currency(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    name TEXT,
    iso_code VARCHAR(3) NOT NULL
);

INSERT INTO currency(name, iso_code)
VALUES ('Euro', 'EUR'),
       ('Pound sterling', 'GBP'),
       ('Swedish krona', 'SEK'),
       ('Unites States dollar', 'USD');