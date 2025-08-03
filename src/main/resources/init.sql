CREATE TABLE IF NOT EXISTS currencies
(
    id INTEGER PRIMARY KEY,
    code TEXT NOT NULL,
    full_name TEXT NOT NULL,
    sign TEXT
);

PRAGMA FOREIGN_KEYS  = ON;

CREATE TABLE IF NOT EXISTS exchange_rates (
    id INTEGER PRIMARY KEY,
    base_currency_id INTEGER REFERENCES currencies(id),
    target_currency_id INTEGER REFERENCES currencies(id),
    rate REAL,

    UNIQUE(base_currency_id, target_currency_id)
)