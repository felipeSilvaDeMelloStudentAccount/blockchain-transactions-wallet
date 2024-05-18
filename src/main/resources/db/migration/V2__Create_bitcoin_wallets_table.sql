CREATE TABLE IF NOT EXISTS bitcoin_wallets
(
    id          BIGSERIAL PRIMARY KEY,
    address     VARCHAR(255),
    balance     BIGINT,
    private_key VARCHAR(255),
    public_key  VARCHAR(255),
    seed_words  TEXT,
    created_at  TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
