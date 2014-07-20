create sequence sch_text_seq start 1 increment by 1;

CREATE TABLE if not exists sch_text
(
  id bigint NOT NULL DEFAULT nextval('sch_text_seq') primary key,
  start_time time without time zone NOT NULL,
  end_time time without time zone NOT NULL,
  info_text text NULL,
  owner_type text NOT NULL,
  owner_id bigint NOT NULL
);

create index sch_text_owner on sch_text (owner_type, owner_id);