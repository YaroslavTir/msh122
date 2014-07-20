create sequence help_info_seq start 1 increment by 1;

create table if not exists help_info (
  id bigint default nextval('help_info_seq') primary key,
  title varchar(1024) not null,
  html_text text not null,
  name varchar(1024) not null,

  object_type varchar(100) not null,
  object_id bigint not null,
  update_date timestamp not null,
  language varchar(100) default 'RU' not null
);

create index help_info_object_idx on help_info (object_type, object_id);