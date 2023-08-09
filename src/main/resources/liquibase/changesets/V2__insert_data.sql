insert into users (name, username, password)
values ('John Doe', 'johndoe@yahoo.com', '$2a$10$yW0whK8qczkFg80PRu0Y7eXaAKobboeT5fpwmS461053ZN88eTSIO'),
       ('Mike Mikenson', 'mikenson@yahoo.com', '$2a$10$62r6hod216U6gTqDbi9ftu2Vjl9.2JOI6513j2nXJSS3lBkfyOyma');

insert into tasks (title, description, status, expiration_date)
values ('Buy cake', null, 'TODO', '2023-01-29 12:00:00'),
       ('Do work', 'Work routine', 'IN_PROGRESS', '2023-01-31 00:00:00'),
       ('Clean room', null, 'DONE', '2023-02-01 00:00:00'),
       ('Call Mike', 'Ask about meeting', 'TODO', '2023-02-01 00:00:00');

insert into users_tasks (task_id, user_id)
values (1, 2),
       (2, 2),
       (3, 2),
       (4, 1);

insert into users_roles (user_id, role)
values (1, 'ROLE_ADMIN'),
       (1, 'ROLE_USER'),
       (2, 'ROLE_USER');