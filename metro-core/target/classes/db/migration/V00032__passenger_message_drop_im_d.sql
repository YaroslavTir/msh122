drop index pass_mess_im_idx;

alter table passenger_message drop column im_id;

alter table passenger_message add column im_name varchar (256) not null;