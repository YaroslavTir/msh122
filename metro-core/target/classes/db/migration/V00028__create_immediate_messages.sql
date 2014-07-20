create sequence imm_message_seq start 1 increment by 1;

CREATE TABLE if not exists imm_message
(
  id bigint NOT NULL DEFAULT nextval('imm_message_seq') primary key,
  update_date timestamp not null,
  start_stop_date timestamp,
  owner_type text not null,
  owner_id bigint not null,

  content_type text not null,
  file_url text,
  info_text text,
  audio_url text,
  bg_color text,
  media_content_id bigint null references media_content(id),
  status text
);

create index i_imm_message_owner on imm_message(owner_type, owner_id);
create index i_imm_message_status on imm_message(status);

