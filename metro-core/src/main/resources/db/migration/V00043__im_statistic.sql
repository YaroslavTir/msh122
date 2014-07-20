create sequence im_stat_seq start 1 increment by 1;
create table if not exists im_statistic (
  id bigint default nextval('im_stat_seq') primary key,
  im_name varchar(100) not null,
  date_from timestamp not null,
  date_to timestamp not null
);

create sequence im_event_seq start 1 increment by 1;
create table if not exists im_event (
  id bigint default nextval('im_event_seq') primary key,
  statistic_id bigint references im_statistic(id),
  event varchar(200) not null,
  fires int not null
);
create index ev_stat_idx on im_event (statistic_id);