DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM albums;
DELETE FROM employees;
DELETE FROM departments;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin');

INSERT INTO user_roles (role, user_id)
VALUES ('ARCHIVE_WORKER', 100000),
       ('ADMIN', 100001),
       ('ARCHIVE_WORKER', 100001);

INSERT INTO departments (name)
VALUES ('KTK-40'),
       ('SKB-3'),
       ('OTD 49/3');

INSERT INTO employees (name, phone_number, department_id)
VALUES ('Naumkin A.', '1-31-65', 100002),
       ('Yachuk M.', '1-32-65', 100002),
       ('Nastenko I.', '1-33-65', 100002),
       ('Epifanov I.', '1-34-17', 100003),
       ('Strelnikov P.', '1-35-17', 100003),
       ('Larionova Y.', '1-29-74', 100004);

INSERT INTO albums (decimal_number, stamp, holder_id)
VALUES ('ВУИА.444444.005', 'I_STAMP', 100005),
       ('ВУИА.444444.006', 'I_STAMP', 100005),
       ('ВУИА.444444.007', 'I_STAMP', 100005),
       ('ВУИА.444444.008', 'I_STAMP', 100006);