create sequence schedule_seq start 1 increment by 1;

CREATE TABLE if not exists schedule
(
  id bigint NOT NULL DEFAULT nextval('schedule_seq') primary key,
  type text not null,
  owner_type text not null,
  owner_id bigint not null,
  start_date date,
  end_date date);

create unique index i_schedule_start_date on schedule(start_date);
create unique index i_schedule_end_date on schedule(end_date);

create sequence sch_content_seq start 1 increment by 1;

CREATE TABLE if not exists sch_content
(
  id bigint NOT NULL DEFAULT nextval('sch_content_seq') primary key,
  sch_id bigint not null references schedule(id) on delete cascade,
  start_time time without time zone NOT NULL,
  end_time time without time zone NOT NULL,
  content_type text,
  file_url text,
  info_text text,
  audio_url text,
  bg_color text
);

create index sch_cont_schedule on sch_content (sch_id);
