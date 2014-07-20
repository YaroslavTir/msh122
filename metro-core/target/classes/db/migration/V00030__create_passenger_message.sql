create sequence pass_mess_seq start 1 increment by 1;

create table if not exists passenger_message (
  id bigint default nextval('pass_mess_seq') primary key,
  im_id bigint references im(id),
  first_name varchar(256) not null,
  last_name varchar(256) not null,
  email varchar(256) not null,
  phone_number varchar(256) not null,
  message text not null,
  create_date timestamp not null
);

create index pass_mess_im_idx on passenger_message (im_id);