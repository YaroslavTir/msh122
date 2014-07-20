alter table im add column static_data_update_date timestamp not null default current_timestamp;
alter table lobby add column static_data_update_date timestamp not null default current_timestamp;
alter table station add column static_data_update_date timestamp not null default current_timestamp;
alter table line add column static_data_update_date timestamp not null default current_timestamp;
alter table schema add column static_data_update_date timestamp not null default current_timestamp;

alter table im add column screensaver varchar(1024);
alter table lobby add column screensaver varchar(1024);
alter table station add column screensaver varchar(1024);
alter table line add column screensaver varchar(1024);
alter table schema add column screensaver varchar(1024);

