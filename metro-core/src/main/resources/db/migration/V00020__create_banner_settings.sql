create sequence banner_settings_seq start 1 increment by 1;

CREATE TABLE if not exists banner_settings
(
  id bigint NOT NULL DEFAULT nextval('banner_settings_seq') primary key,
  update_date timestamp not null,
  owner_type text not null,
  owner_id bigint not null
);

create unique index i_banner_owner on banner_settings(owner_type, owner_id);

create index i_schedule_owner on schedule(owner_type, owner_id);

create sequence banner_video_seq start 1 increment by 1;

CREATE TABLE if not exists banner_video
(
  id bigint NOT NULL DEFAULT nextval('banner_video_seq') primary key,
  number smallint not null,
  banner_settings_id bigint not null references banner_settings(id) on delete cascade,
  media_content_id bigint not null references media_content(id) on delete cascade
);

create index i_banner_video_settings_fk on banner_video(banner_settings_id);


