CREATE TABLE IF NOT EXISTS person_resolution (
    id SERIAL NOT NULL PRIMARY KEY,
    created_at timestamp DEFAULT now(),
    individual_matches int DEFAULT 0,
    household_matches int DEFAULT 0,
    nomatches int DEFAULT 0,
    errors int DEFAULT 0,
    CONSTRAINT person_res_id UNIQUE (id)
);