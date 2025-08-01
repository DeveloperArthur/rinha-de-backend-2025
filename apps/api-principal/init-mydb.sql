CREATE TYPE payment_processors AS ENUM ('default', 'fallback');

CREATE TABLE processed_payments (
    id SERIAL PRIMARY KEY,
    processor_name payment_processors NOT NULL,
    processed_at TIMESTAMP NOT NULL DEFAULT NOW(),
    amount REAL NOT NULL
);

CREATE TABLE sync_pendents_payments (
    id SERIAL PRIMARY KEY,
    correlation_id UUID NOT NULL,
    amount REAL NOT NULL
);





