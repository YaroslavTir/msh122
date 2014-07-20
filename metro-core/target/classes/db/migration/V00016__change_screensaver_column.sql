alter table im drop column screensaver;
alter table lobby drop column screensaver;
alter table station drop column screensaver;
alter table line drop column screensaver;
alter table schema drop column screensaver;

alter table im add column screensaver bigint references media(id);
alter table lobby add column screensaver bigint references media(id);
alter table station add column screensaver bigint references media(id);
alter table line add column screensaver bigint references media(id);
alter table schema add column screensaver bigint references media(id);

create index im_scrsvr_media_idx on im (screensaver);
create index lobby_scrsvr_media_idx on lobby (screensaver);
create index station_scrsvr_media_idx on station (screensaver);
create index line_scrsvr_media_idx on line (screensaver);
create index schema_scrsvr_media_idx on schema (screensaver);

