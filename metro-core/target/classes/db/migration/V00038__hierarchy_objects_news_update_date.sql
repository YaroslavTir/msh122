alter table im add column news_update_date timestamp not null default current_timestamp;
alter table lobby add column news_update_date timestamp not null default current_timestamp;
alter table station add column news_update_date timestamp not null default current_timestamp;
alter table line add column news_update_date timestamp not null default current_timestamp;
alter table schema add column news_update_date timestamp not null default current_timestamp;
