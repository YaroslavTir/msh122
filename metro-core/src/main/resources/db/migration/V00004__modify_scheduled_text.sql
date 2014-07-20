alter table sch_text add start_hour smallint not null;
alter table sch_text add end_hour smallint not null;

create index sch_text_start_hour on sch_text(start_hour);
create index sch_text_end_hour on sch_text(end_hour);