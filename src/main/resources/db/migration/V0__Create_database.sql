DO
$$
    BEGIN
        -- Check if the database does not exist
        IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'blockchain_db') THEN
            -- Create the database if it does not exist
            CREATE DATABASE blockchain_db;
        END IF;
    END
$$;
