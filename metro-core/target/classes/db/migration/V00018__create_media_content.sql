create sequence media_content_seq start 1 increment by 1;

CREATE TABLE if not exists media_content
(
  id bigint NOT NULL DEFAULT nextval('media_content_seq') primary key,
  update_date timestamp not null,
  type text,
  file_url text,
  audio_url text
);

create index i_media_content_file_url on media_content (file_url);
create index i_media_content_audio_url on media_content (audio_url);

alter table sch_content add media_content_id bigint null references media_content(id);