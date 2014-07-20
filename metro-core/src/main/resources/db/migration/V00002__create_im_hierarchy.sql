create sequence schema_seq start 1 increment by 1;
create sequence line_seq start 1 increment by 1;
create sequence station_seq start 1 increment by 1;
create sequence lobby_seq start 1 increment by 1;
create sequence im_seq start 1 increment by 1;

create table if not exists schema (
  id bigint default nextval('schema_seq') primary key,
  name varchar(200) unique not null
);

create table if not exists line (
  id bigint default nextval('line_seq') primary key,
  schema_id bigint references schema(id),
  name varchar(200) unique not null,
  number smallint unique not null,
  pic_link text
);
create index line_schema_idx on line (schema_id);

create table if not exists station (
  id bigint default nextval('station_seq') primary key,
  line_id bigint references line(id),
  name varchar(200) not null
);
create index station_line_idx on station (line_id);

create table if not exists lobby (
  id bigint default nextval('lobby_seq') primary key,
  station_id bigint references station(id),
  name varchar(200) not null
);
create index lobby_station_idx on lobby (station_id);

create table if not exists im (
  id bigint default nextval('im_seq') primary key,
  lobby_id bigint references lobby(id),
  name varchar(100) unique not null,
  im_name varchar(100) unique not null,
  ip varchar(100) not null,
  position varchar(512) not null
);
create index im_lobby_idx on im (lobby_id);