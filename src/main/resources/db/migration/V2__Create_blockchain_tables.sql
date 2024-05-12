-- V2__Create_blockchain_tables.sql

-- Create blocks table
CREATE TABLE blocks (
                        hash VARCHAR(255) PRIMARY KEY,
                        previous_hash VARCHAR(255),
                        nonce BIGINT,
                        difficulty BIGINT,
                        timestamp TIMESTAMP WITHOUT TIME ZONE,
                        CONSTRAINT fk_block_previous_hash FOREIGN KEY (previous_hash) REFERENCES blocks (hash)
);

-- Create transactions table
CREATE TABLE transactions (
                              transaction_id VARCHAR(255) PRIMARY KEY,
                              block_hash VARCHAR(255),
                              CONSTRAINT fk_transaction_block FOREIGN KEY (block_hash) REFERENCES blocks (hash) ON DELETE CASCADE
);

-- Create transaction_inputs table
CREATE TABLE transaction_inputs (
                                    id BIGSERIAL PRIMARY KEY,
                                    source_transaction_id VARCHAR(255),
                                    output_index INTEGER,
                                    script_sig TEXT,
                                    transaction_id VARCHAR(255),
                                    CONSTRAINT fk_input_transaction FOREIGN KEY (transaction_id) REFERENCES transactions (transaction_id) ON DELETE CASCADE
);

-- Create transaction_outputs table
CREATE TABLE transaction_outputs (
                                     id BIGSERIAL PRIMARY KEY,
                                     value DECIMAL(18, 8),
                                     script_pub_key TEXT,
                                     transaction_id VARCHAR(255),
                                     CONSTRAINT fk_output_transaction FOREIGN KEY (transaction_id) REFERENCES transactions (transaction_id) ON DELETE CASCADE
);
