create sequence station_plan_settings_seq start 1 increment by 1;

CREATE TABLE if not exists station_plan_settings
(
  id bigint NOT NULL DEFAULT nextval('station_plan_settings_seq') primary key,

  rus_url text,
  eng_url text,
  owner_type text NOT NULL,
  owner_id bigint NOT NULL
);

create unique index i_station_plan_settings_owner on station_plan_settings (owner_type, owner_id);