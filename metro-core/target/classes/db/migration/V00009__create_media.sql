create sequence media_seq start 1 increment by 1;

CREATE TABLE if not exists media
(
  id bigint NOT NULL DEFAULT nextval('media_seq') primary key,
  name text not null,
  created_date timestamp not null,
  url text,
  media_type text,
  thumb_url text
);

create unique index i_media_url on media (url);