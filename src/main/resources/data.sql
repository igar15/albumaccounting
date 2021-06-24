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
VALUES ('Отдел № 1'),
       ('Отдел № 3'),
       ('Отдел № 2');

INSERT INTO employees (name, phone_number, department_id)
VALUES ('Петров П.П.', '1-11-11', 100002),
       ('Сидоров С.С.', '1-22-22', 100002),
       ('Иванов И.И.', '1-33-33', 100002),
       ('Гусева Н.Н.', '1-11-11', 100003),
       ('Баранов И.И.', '1-22-33', 100003),
       ('Быстрова С.В.', '1-22-55', 100004);

INSERT INTO albums (decimal_number, stamp, location, holder_id)
VALUES ('АБВГ.444444.005', 'I_STAMP', 'С-3/П-1', 100005),
       ('АБВГ.444444.006', 'I_STAMP', 'С-3/П-1', 100005),
       ('АБВГ.444444.007', 'I_STAMP', 'С-3/П-1', 100005),
       ('АБВГ.444444.008', 'I_STAMP', 'С-3/П-1', 100006);