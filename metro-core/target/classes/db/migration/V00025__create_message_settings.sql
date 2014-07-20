create sequence message_schedule_seq start 1 increment by 1;

CREATE TABLE if not exists message_schedule
(
  id bigint NOT NULL DEFAULT nextval('message_schedule_seq') primary key,
  update_date timestamp not null,
  owner_type text not null,
  owner_id bigint not null
);

create unique index i_message_schedule_owner on message_schedule(owner_type, owner_id);

