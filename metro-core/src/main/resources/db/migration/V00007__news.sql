create sequence news_seq start 1 increment by 1;

create table if not exists news (
  id bigint default nextval('news_seq') primary key,
  title varchar(1024) not null,
  text text not null,
  object_type varchar(100) not null,
  object_id bigint not null,
  update_date timestamp not null
);

create index news_object_idx on news (object_type, object_id);