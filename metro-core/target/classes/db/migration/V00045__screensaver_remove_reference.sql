alter table im add column screensaver_url varchar(1024);
alter table lobby add column screensaver_url varchar(1024);
alter table station add column screensaver_url varchar(1024);
alter table line add column screensaver_url varchar(1024);
alter table schema add column screensaver_url varchar(1024);

update im
set screensaver_url = mf.url
from (select id, url from media) as mf
where im.screensaver = mf.id;

update lobby
set screensaver_url = mf.url
from (select id, url from media) as mf
where lobby.screensaver = mf.id;

update station
set screensaver_url = mf.url
from (select id, url from media) as mf
where station.screensaver = mf.id;

update line
set screensaver_url = mf.url
from (select id, url from media) as mf
where line.screensaver = mf.id;

update schema
set screensaver_url = mf.url
from (select id, url from media) as mf
where schema.screensaver = mf.id;

alter table im drop column screensaver;
alter table lobby drop column screensaver;
alter table station drop column screensaver;
alter table line drop column screensaver;
alter table schema drop column screensaver;