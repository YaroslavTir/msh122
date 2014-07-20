alter table passenger_message drop column first_name;
alter table passenger_message drop column last_name;
alter table passenger_message drop column email;
alter table passenger_message drop column phone_number;

alter table passenger_message add column name varchar(1024) default 'anonymous' not null;
alter table passenger_message add column contact varchar(1024) default 'anonymous' not null;