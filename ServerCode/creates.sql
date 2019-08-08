--Create tables for self-advocacy app
Drop table Users;
Drop table Events;


create table Users
(
	userID int not null auto_increment primary key,
	userName varchar(255) not null unique,
	firstName varchar(255) not null,
	surname varchar(255) not null,
	password varchar(255) not null,
	level int not null -- 0 = basic users, 1 = admin
);

create table Events
(
	eventID int not null auto_increment primary key,
	eventName varchar(255) not null,
	eventDesc varchar(255) not null,
	eventDate datetime not null
);