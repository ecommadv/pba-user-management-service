CREATE TABLE IF NOT EXISTS active_user (
    id SERIAL,
    uid uuid NOT NULL,
    username character varying NOT NULL,
    password character varying NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS pending_user (
    id SERIAL,
    uid uuid NOT NULL,
    username character varying NOT NULL,
    password character varying NOT NULL,
    created_at timestamp NOT NULL,
    validation_code uuid NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS active_user_profile (
    id SERIAL,
    firstname character varying NOT NULL,
    lastname character varying NOT NULL,
    email character varying NOT NULL,
    country character varying,
    age INTEGER,
    user_id bigint NOT NULL,
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
            REFERENCES active_user(id),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS pending_user_profile (
    id SERIAL,
    firstname character varying NOT NULL,
    lastname character varying NOT NULL,
    email character varying NOT NULL,
    user_id bigint NOT NULL,
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
            REFERENCES pending_user(id),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS group_type (
    id SERIAL,
    name character varying NOT NULL,
    uid uuid NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user_type (
    id SERIAL,
    name character varying NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS group_member (
    id SERIAL,
    user_id bigint NOT NULL,
    user_type_id bigint NOT NULL,
    group_id bigint NOT NULL,
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
            REFERENCES active_user(id),
    CONSTRAINT fk_user_type_id
        FOREIGN KEY (user_type_id)
            REFERENCES user_type(id),
    CONSTRAINT fk_group_id
        FOREIGN KEY (group_id)
            REFERENCES group_type(id),
    PRIMARY KEY (id)
);
