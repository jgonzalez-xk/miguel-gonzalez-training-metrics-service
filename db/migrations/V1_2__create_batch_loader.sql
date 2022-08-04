CREATE TABLE IF NOT EXISTS batch_loader (
    id SERIAL NOT NULL PRIMARY KEY,
    created_at timestamp DEFAULT now(),
    file_name varchar(255) NULL,
    file_created_at timestamp NULL,
    file_pickedup_at timestamp NULL,
    file_processed_at timestamp NULL,
    num_records int DEFAULT 0,
    CONSTRAINT batch_loader_id UNIQUE (id)
);