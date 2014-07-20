alter table help_info add help_info_type varchar(100) default 'HTML' not null;
alter table help_info add videos bytea;

ALTER TABLE help_info ALTER COLUMN html_text DROP NOT NULL;
ALTER TABLE help_info ALTER COLUMN title DROP NOT NULL;

