DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS albums;
DROP TABLE IF EXISTS employees;
DROP TABLE IF EXISTS departments;
DROP SEQUENCE IF EXISTS global_seq;

CREATE SEQUENCE global_seq START WITH 100000;

CREATE TABLE users
(
    id         INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    name       VARCHAR                           NOT NULL,
    email      VARCHAR                           NOT NULL,
    password   VARCHAR                           NOT NULL,
    enabled    BOOL                DEFAULT TRUE  NOT NULL,
    registered TIMESTAMP           DEFAULT now() NOT NULL
);
CREATE UNIQUE INDEX users_unique_email_idx ON users (email);

CREATE TABLE user_roles
(
    user_id INTEGER NOT NULL,
    role    VARCHAR,
    CONSTRAINT user_roles_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE departments
(
    id   INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    name VARCHAR NOT NULL
);
CREATE UNIQUE INDEX departments_unique_name_idx ON departments (name);

CREATE TABLE employees
(
    id            INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    name          VARCHAR NOT NULL,
    phone_number  VARCHAR NOT NULL,
    department_id INTEGER NOT NULL,
    FOREIGN KEY (department_id) REFERENCES departments (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX employees_unique_name_phone_number_idx ON employees (name, phone_number);

CREATE TABLE albums
(
    id             INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    decimal_number VARCHAR NOT NULL,
    stamp          VARCHAR NOT NULL,
    location       VARCHAR NOT NULL,
    holder_id      INTEGER NOT NULL,
    FOREIGN KEY (holder_id) REFERENCES employees (id)
);
CREATE UNIQUE INDEX albums_unique_decimal_number_stamp_idx ON albums (decimal_number, stamp);