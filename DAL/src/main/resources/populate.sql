/*Before executing this script(populate.sql), make sure you have previously executed the pm.sql script*/
/*Run all the ALTER statements below in order to avoid foreign key violations in the later DML statements*/
ALTER SEQUENCE users_id_seq RESTART WITH 1;
ALTER SEQUENCE destinations_id_seq RESTART WITH 1;

insert into users(last_name, first_name, email, password)
values
('Balogh', 'Luca', 'luca@gmail.com', 'luca'),
('Butincu', 'Filip', 'filip@gmail.com', 'filip'),
('Veres', 'Ben', 'ben@gmail.com', 'ben');

insert into movies(name, tip, rating, user_id)
values
('Deadpool','Action', 9.9, 1),
('Deadpool 2','Action', 10, 3),
('Indiana Jones','Adventure', 9.6, 2),
('Dumb and dumber','Comedy', 9.0, 1);





