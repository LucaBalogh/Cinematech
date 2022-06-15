/*Before executing this script(populate.sql), make sure you have previously executed the pm.sql script*/
/*Run all the ALTER statements below in order to avoid foreign key violations in the later DML statements*/
ALTER SEQUENCE users_id_seq RESTART WITH 1;
ALTER SEQUENCE destinations_id_seq RESTART WITH 1;

insert into users(last_name, first_name, email, password)
values
('Balogh', 'Luca', 'luca@gmail.com', 'luca'),
('Butincu', 'Filip', 'filip@gmail.com', 'filip'),
('Veres', 'Ben', 'ben@gmail.com', 'ben');

insert into locations(city, country, tip, rating, user_id)
values
('Ibiza','Spain','Sea', 5.5, 1),
('Vienna','Austria','Mountain', 7.5, 3),
('Minsk','Bulgaria','Sea', 8.5, 2),
('Hong Kong','China','Mountain', 9.0, 1);





