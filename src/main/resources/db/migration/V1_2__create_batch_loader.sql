CREATE TABLE IF NOT EXISTS batch_loader (
    id SERIAL NOT NULL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    file_name VARCHAR(255) NULL,
    file_created_at TIMESTAMP NULL,
    file_pickedup_at TIMESTAMP NULL,
    file_processed_at TIMESTAMP NULL,
    num_records INT DEFAULT 0,
    CONSTRAINT batch_loader_id UNIQUE (id)
);