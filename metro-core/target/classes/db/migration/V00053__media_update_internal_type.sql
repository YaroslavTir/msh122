update sch_content set content_type='IMAGE' where content_type in ('IMAGE_TEXT', 'BGCOLOR_TEXT', 'IMAGE_AUDIO', 'IMAGE_TEXT_AUDIO', 'BGCOLOR_TEXT_AUDIO');
update imm_message set content_type='IMAGE' where content_type in ('IMAGE_TEXT', 'BGCOLOR_TEXT', 'IMAGE_AUDIO', 'IMAGE_TEXT_AUDIO', 'BGCOLOR_TEXT_AUDIO');

