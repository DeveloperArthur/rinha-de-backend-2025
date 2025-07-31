SELECT * FROM pg_catalog.pg_tables WHERE schemaname = 'public';


CREATE TYPE payment_processors AS ENUM ('default', 'fallback');

CREATE TABLE processed_payments (
                                    id SERIAL PRIMARY KEY,
                                    processor_name payment_processors NOT NULL,
                                    processed_at TIMESTAMP NOT NULL DEFAULT NOW(),
                                    amount REAL NOT NULL
);

CREATE TABLE sync_pendents_payments (
                                        id SERIAL PRIMARY KEY,
                                        request_body TEXT NOT NULL
)

select * from processed_payments

    insert into processed_payments (processor_name, processed_at, amount) VALUES ('default', '2025-07-15T12:34:56.000Z', 19.90)

ALTER TABLE processed_payments
    RENAME COLUMN processedAt TO processed_at;

INSERT INTO clientes (nome, limite, saldo)
VALUES
    ('o barato sai caro', 1000 * 100, 0)


    INSERT INTO processed_payments (processor_name, processed_at, amount)
VALUES ('default','2025-07-15 12:34:56',13.8)

select * from payments

ALTER TABLE processed_payments
    RENAME TO payments;

ALTER TABLE payments
    ADD COLUMN is_processed BOOLEAN NOT NULL DEFAULT FALSE;



ALTER TABLE payments
    ALTER COLUMN processor_name DROP NOT NULL;



INSERT INTO processed_payments (processor_name, processed_at, amount)
VALUES ('default','2025-07-15 12:34:56',13.8)

select * from processed_payments

ALTER TABLE payments
    RENAME TO processed_payments;

insert into processed_payments (processed_at, amount, is_processed) VAlues ('2025-07-15 12:34:56', 10, FALSE)

ALTER TABLE payments
    ADD COLUMN is_processed BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE processed_payments
    ALTER COLUMN processor_name SET NOT NULL;

update processed_payments set processor_name = 'default'  where processor_name = null;

delete from processed_payments where id = 20





CREATE TABLE sync_pendents_payments (
                                        id SERIAL PRIMARY KEY,
                                        correlation_id UUID NOT NULL,
                                        amount REAL NOT NULL
);

ALTER TABLE sync_pendents_payments
    ADD COLUMN correlation_id UUID NOT NULL,
    ADD COLUMN amount REAL NOT NULL;

ALTER TABLE sync_pendents_payments
DROP COLUMN request_body;