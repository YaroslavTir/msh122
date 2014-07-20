ALTER TABLE schema add update_date timestamp not null default now();
ALTER TABLE line add update_date timestamp not null default now();
ALTER TABLE station add update_date timestamp not null default now();
ALTER TABLE lobby add update_date timestamp not null default now();
ALTER TABLE im add update_date timestamp not null default now();