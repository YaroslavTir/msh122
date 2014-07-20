alter table imm_message drop constraint imm_message_media_content_id_fkey;

alter table imm_message add CONSTRAINT imm_message_media_content_id_fkey FOREIGN KEY (media_content_id)
REFERENCES media_content (id) ON DELETE SET NULL;