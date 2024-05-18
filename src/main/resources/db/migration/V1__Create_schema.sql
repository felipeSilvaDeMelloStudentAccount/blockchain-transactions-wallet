-- Grant privileges to the user
GRANT ALL PRIVILEGES ON DATABASE blockchain_db TO blockchain;

-- Drop constraints if they exist
ALTER TABLE IF EXISTS transaction_inputs
    DROP CONSTRAINT IF EXISTS fk_inputs;
ALTER TABLE IF EXISTS transaction_outputs
    DROP CONSTRAINT IF EXISTS fk_outputs;

-- Drop tables if they exist
DROP TABLE IF EXISTS transaction_inputs CASCADE;
DROP TABLE IF EXISTS transaction_outputs CASCADE;
DROP TABLE IF EXISTS transactions CASCADE;
DROP TABLE IF EXISTS blocks CASCADE;

-- Create the blocks table
CREATE TABLE IF NOT EXISTS blocks
(
    hash          VARCHAR PRIMARY KEY,
    previous_hash VARCHAR,
    nonce         BIGINT,
    difficulty    BIGINT,
    timestamp     TIMESTAMPTZ
);

-- Create the transactions table
CREATE TABLE IF NOT EXISTS transactions
(
    transaction_id VARCHAR PRIMARY KEY,
    block_hash     VARCHAR REFERENCES blocks (hash)
);

-- Create the transaction_inputs table
CREATE TABLE IF NOT EXISTS transaction_inputs
(
    id                    SERIAL PRIMARY KEY,
    source_transaction_id VARCHAR,
    output_index          INT,
    script_sig            VARCHAR,
    transaction_id        VARCHAR REFERENCES transactions (transaction_id)
);

-- Create the transaction_outputs table
CREATE TABLE IF NOT EXISTS transaction_outputs
(
    id             SERIAL PRIMARY KEY,
    value          DECIMAL,
    script_pub_key VARCHAR,
    transaction_id VARCHAR REFERENCES transactions (transaction_id)
);
