CREATE TABLE currency(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    created_at timestamp default now(),
    updated_at timestamp default now(),
    name TEXT,
    iso_code VARCHAR(3) NOT NULL
);

INSERT INTO currency(name, iso_code)
VALUES ('Euro', 'EUR'),
       ('Pound sterling', 'GBP'),
       ('Swedish krona', 'SEK'),
       ('Unites States dollar', 'USD');