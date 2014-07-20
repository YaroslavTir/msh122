drop index i_schedule_start_date;
drop index i_schedule_end_date;

create index i_schedule_start_date on schedule(start_date);
create index i_schedule_end_date on schedule(end_date);