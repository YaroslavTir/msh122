alter table media_content add generated boolean not null default false;

alter table sch_content drop constraint sch_content_media_content_id_fkey;
alter table sch_content add CONSTRAINT sch_content_media_content_id_fkey FOREIGN KEY (media_content_id)
REFERENCES media_content (id) ON DELETE SET NULL;