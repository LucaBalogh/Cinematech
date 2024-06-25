/*Run all the DROP statements below in order to recreate all the tables*/
/*
drop table movies;
drop table users;
*/

CREATE TABLE users(
	id serial,
	last_name varchar(255),
	first_name varchar(255),
	email varchar(255) not null unique,
	password varchar(255) not null,
	CONSTRAINT pk_users PRIMARY KEY(id)
);

CREATE TABLE movies(
	id serial,
	name varchar(255) not null,
	tip varchar(255) not null,
	rating float,
	user_id int,
	CONSTRAINT fk_users_roles FOREIGN KEY(user_id) REFERENCES users(id),
	CONSTRAINT pk_roles PRIMARY KEY(id)
);