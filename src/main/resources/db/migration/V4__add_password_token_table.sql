CREATE TABLE IF NOT EXISTS password_token (
    id SERIAL,
    user_id bigint NOT NULL,
    token uuid NOT NULL,
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
            REFERENCES active_user(id),
    PRIMARY KEY (id)
);