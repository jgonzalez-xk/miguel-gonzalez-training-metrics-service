CREATE TABLE IF NOT EXISTS person_resolution (
    id SERIAL NOT NULL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    individual_matches INT DEFAULT 0,
    household_matches INT DEFAULT 0,
    nomatches INT DEFAULT 0,
    errors INT DEFAULT 0,
    endpoint VARCHAR(255),
    CONSTRAINT person_res_id UNIQUE (id)
);