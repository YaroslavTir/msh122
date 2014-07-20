alter table settings add update_date timestamp not null default now();

alter table imm_message add start_date timestamp;
alter table imm_message add stop_date timestamp;

update imm_message set start_date=start_stop_date where status='STARTED';
update imm_message set start_date=start_stop_date, stop_date=start_stop_date where status='STOPPED';

alter table imm_message drop column start_stop_date;