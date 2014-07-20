package com.fls.metro.core.data.dao

import com.fls.metro.core.data.domain.GeneratedMediaContent
import com.fls.metro.core.data.domain.ImmediateMessage
import com.fls.metro.core.data.domain.ObjectType
import com.fls.metro.core.data.dto.content.media.MediaContentInternal
import com.fls.metro.core.data.dto.content.media.MediaContentInternalSize
import com.fls.metro.core.data.dto.content.media.MediaContentInternalType
import com.fls.metro.core.data.dto.messages.ImmMessageStatus
import groovy.sql.Sql
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
/**
 * User: NFadin
 * Date: 16.04.14
 * Time: 13:11
 */
@Slf4j
@Repository
class ImmediateMessageDao extends AbstractDao<ImmediateMessage> {


    @Autowired
    GeneratedMediaContentDao generatedMediaDao;

    ImmediateMessage find(Long id){
        def it=sql.firstRow('select id, owner_type, owner_id, update_date, start_date, stop_date, status, content_type, media_size, file_url, info_text, audio_url, bg_color,media_content_id from imm_message where id=? ',[id]);
        if(it){
            ImmediateMessage imm=new ImmediateMessage(id:it.id, ownerType: ObjectType.valueOf(it.owner_type), ownerId: it.owner_id, startDate: it.start_date, stopDate:it.stop_date, updateDate: it.update_date, status: ImmMessageStatus.valueOf(it.status),
                    content:(it.content_type==null?null:new MediaContentInternal(type:MediaContentInternalType.valueOf(it.content_type), size:it.media_size?MediaContentInternalSize.valueOf(it.media_size):MediaContentInternalSize.DEFAULT, infoText:it.info_text, fileUrl:it.file_url, audioUrl: it.audio_url, bgColor: it.bg_color)));
            if(it.media_content_id!=null){
                imm.contentExt=generatedMediaDao.find(it.media_content_id);
            }
            return imm;
        }
        return null;
    }

    List<ImmediateMessage> list(ObjectType ownerType, Long ownerId) {
        List<ImmediateMessage> result = []
        sql.eachRow('select id, update_date, start_date, stop_date, status, content_type, media_size, file_url, info_text, audio_url, bg_color,media_content_id from imm_message where owner_type=? and owner_id=? order by update_date',[ownerType.name(), ownerId]) {
            ImmediateMessage imm=new ImmediateMessage(id:it.id,ownerType: ownerType, ownerId:ownerId,
                    startDate: it.start_date, stopDate:it.stop_date, updateDate: it.update_date, status: ImmMessageStatus.valueOf(it.status),
                    content:(it.content_type==null?null:new MediaContentInternal(type:MediaContentInternalType.valueOf(it.content_type), size:it.media_size?MediaContentInternalSize.valueOf(it.media_size):MediaContentInternalSize.DEFAULT,infoText:it.info_text, fileUrl:it.file_url, audioUrl: it.audio_url, bgColor: it.bg_color)));
            if(it.media_content_id!=null){
                imm.contentExt=generatedMediaDao.find(it.media_content_id);
            }else{
                imm.contentExt=new GeneratedMediaContent();
            }
            result.add(imm)
        }
        result
    }

    ImmediateMessage getCurrentMessage(ObjectType ownerType, Long ownerId){
        def it=sql.firstRow('select id, update_date, start_date, stop_date, content_type, media_size, info_text, file_url, audio_url, bg_color,media_content_id from imm_message where status=? and owner_type=? and owner_id=?',[ImmMessageStatus.STARTED.name(), ownerType.name(), ownerId])
        if(it!=null){
            ImmediateMessage result=new ImmediateMessage(id:it.id,ownerType: ownerType, ownerId: ownerId,
                    updateDate: it.update_date, startDate: it.start_date, stopDate:it.stop_date, status: ImmMessageStatus.STARTED,
                    content:(it.content_type==null?null:new MediaContentInternal(type:MediaContentInternalType.valueOf(it.content_type), size:it.media_size?MediaContentInternalSize.valueOf(it.media_size):MediaContentInternalSize.DEFAULT, infoText:it.info_text, fileUrl:it.file_url, audioUrl: it.audio_url, bgColor: it.bg_color)));
            if(it.media_content_id!=null){
                result.contentExt=generatedMediaDao.find(it.media_content_id);
            }
            result
        }else{
            null
        }
    }

    ImmediateMessage create(ImmediateMessage imm){
        if(imm.content!=null){
            def result=sql.executeInsert("insert into imm_message(update_date, owner_type, owner_id, status," +
                "content_type, media_size, file_url, info_text, audio_url,bg_color, media_content_id) " +
                "values (?,?,?,?,?,?,?,?,?,?,?) ",
                [Sql.TIMESTAMP(imm.updateDate), imm.ownerType.name(), imm.ownerId, imm.status.name(),
                imm.content.type.name(), imm.content.size?imm.content.size.name():null, imm.content.fileUrl,imm.content.infoText,imm.content.audioUrl,imm.content.bgColor,
                imm.contentExt!=null?imm.contentExt.id:null]);
            Long id=result[0][0];
            imm.id=id;
            imm
        }else{
            def result=sql.executeInsert("insert into imm_message(update_date, owner_type, owner_id, status, media_content_id) " +
                    "values (?,?,?,?,?) ",
                    [Sql.TIMESTAMP(imm.updateDate), imm.ownerType.name(), imm.ownerId, imm.status, imm.contentExt!=null?imm.contentExt.id:null]);
            Long id=result[0][0];
            imm.id=id;
            imm
        }

    }

    ImmediateMessage update(ImmediateMessage imm){
        if(imm.content!=null){
            sql.execute("update imm_message set update_date=?," +
                    "content_type=?, media_size=?, file_url=?, info_text=?, audio_url=?,bg_color=?, media_content_id=? where id=?",
                    [Sql.TIMESTAMP(imm.updateDate),
                            imm.content.type.name(), imm.content.size?imm.content.size.name():null, imm.content.fileUrl,imm.content.infoText,imm.content.audioUrl,imm.content.bgColor,
                            imm.contentExt!=null?imm.contentExt.id:null, imm.id]);
            imm
        }else{
            sql.execute("update imm_messages set update_date=?, media_content_id=? where id=?",
                    [Sql.DATE(imm.updateDate), imm.contentExt!=null?imm.contentExt.id:null]);
            imm
        }

    }

    void updateStatus(Long id, ImmMessageStatus status){
        if(status==ImmMessageStatus.STARTED){
            sql.execute("update imm_message set start_date=?, status=? where id=?",
                [Sql.TIMESTAMP(new Date()), status.name(), id]);
        }else{
            sql.execute("update imm_message set stop_date=?, status=? where id=?",
                    [Sql.TIMESTAMP(new Date()), status.name(), id]);
        }
    }

    void deleteFor(ObjectType type, Long ownerId){
        sql.execute("delete from imm_message where owner_type=? and owner_id=?",[type.name(), ownerId]);
    }

}
