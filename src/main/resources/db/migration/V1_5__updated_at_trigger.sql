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