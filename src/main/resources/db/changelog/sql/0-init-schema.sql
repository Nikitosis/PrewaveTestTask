CREATE TABLE edge
(
    from_id INT NOT NULL,
    to_id INT NOT NULL,
    PRIMARY KEY (from_id, to_id)
);

-- created index for faster search
CREATE INDEX idx_from_id ON edge(from_id);

