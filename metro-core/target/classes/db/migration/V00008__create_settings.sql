create sequence settings_seq start 1 increment by 1;

CREATE TABLE if not exists settings
(
  id bigint NOT NULL DEFAULT nextval('settings_seq') primary key,
  emergency_number text,

  show_currency boolean NULL,
  show_weather boolean NULL,
  show_time boolean NULL,
  show_languages boolean NULL,
  show_station_name boolean NULL,

  owner_type text NOT NULL,
  owner_id bigint NOT NULL
);

create unique index settings_owner on settings (owner_type, owner_id);