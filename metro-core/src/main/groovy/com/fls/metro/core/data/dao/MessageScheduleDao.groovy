package com.fls.metro.core.data.dao

import com.fls.metro.core.data.domain.ObjectType
import com.fls.metro.core.data.domain.MessageSchedule
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Repository
/**
 * User: NFadin
 * Date: 16.04.14
 * Time: 13:11
 */
@Slf4j
@Repository
class MessageScheduleDao extends AbstractDao<MessageSchedule> {

    MessageSchedule get(ObjectType type, Long ownerId) {
        MessageSchedule result=null;
        def it=sql.firstRow('select id, update_date from message_schedule where owner_type=? and owner_id=?',[type.name(),ownerId]);
        if(it){
            result=new MessageSchedule(id: it.id, updateDate: it.update_date,ownerType:type, ownerId:ownerId);
        }
        result
    }


    void deleteFor(ObjectType type, Long ownerId){
        sql.execute("delete from message_schedule where owner_type=? and owner_id=?",[type.name(), ownerId]);
    }

}
