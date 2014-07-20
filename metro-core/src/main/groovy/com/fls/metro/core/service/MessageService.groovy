package com.fls.metro.core.service
import com.fls.metro.core.data.dao.ImmediateMessageDao
import com.fls.metro.core.data.dao.MessageScheduleDao
import com.fls.metro.core.data.domain.GeneratedMediaContent
import com.fls.metro.core.data.domain.HierarchyObject
import com.fls.metro.core.data.domain.ImmediateMessage
import com.fls.metro.core.data.domain.MessageSchedule
import com.fls.metro.core.data.domain.ObjectType
import com.fls.metro.core.data.dto.content.Area
import com.fls.metro.core.data.dto.messages.ImmMessageStatus
import com.fls.metro.core.data.dto.schedule.ScheduleType
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
/**
 * Created with IntelliJ IDEA.
 * User: PGrebenyuk
 * Date: 05.05.14
 * Time: 15:51
 * To change this template use File | Settings | File Templates.
 */
@Service
@Slf4j
class MessageService extends TraverseHierarchyService<ImmediateMessage>{

    @Autowired
    private HierarchyDataRetriever hierarchyDataRetriever

    @Autowired
    MessageScheduleDao messageScheduleDao

    @Autowired
    ImmediateMessageDao immediateMessageDao

    @Autowired
    ScheduleService scheduleService

    @Autowired
    MediaContentService mediaContentService

    private final scheduleType=ScheduleType.MESSAGE

    @Transactional
    MessageSchedule createMessageSchedule(MessageSchedule settings){
        settings.updateDate=new Date();
        MessageSchedule created=messageScheduleDao.create(settings);
        settings.setId(created.id);
        if(settings.schedule!=null){
            scheduleService.create(settings.schedule,settings.ownerType, settings.ownerId, scheduleType);
        }
        return settings;
    }

    @Transactional
    void updateMessageSchedule(MessageSchedule settings){
        settings.updateDate=new Date();

        if(settings.schedule!=null){
            scheduleService.updateScheduleFor(settings.schedule,settings.ownerType, settings.ownerId, scheduleType);
        }else{
            scheduleService.cleanScheduleFor(settings.ownerType, settings.ownerId, scheduleType);
        }
        messageScheduleDao.update(settings);
    }


    @Transactional
    MessageSchedule getMessageSchedule(ObjectType type, Long ownerId){
        def result = hierarchyDataRetriever.collectAllLevelsFirstOccurrence(type, ownerId) { ObjectType ot, oid, HierarchyObject o ->
            MessageSchedule ms=messageScheduleDao.get(ot, oid);
            if (ms) {
                ms.schedule=scheduleService.getScheduleFor(ot, oid,scheduleType);
                return ms;
            }
            return null
        }
        if (result) {
            return result[0] as MessageSchedule
        }
        return null

    }

    @Transactional
    void deleteMessageSchedule(ObjectType type, Long ownerId){
        messageScheduleDao.deleteFor(type, ownerId);
    }

    @Transactional
    void deleteMessageSchedule(MessageSchedule ms){
        if(ms.schedule){
            scheduleService.cleanScheduleFor(ms.ownerType, ms.ownerId, scheduleType);
        }
        messageScheduleDao.delete(ms.id);
    }

    @Transactional
    void processMessageScheduleForHierarchyObject(MessageSchedule messageSchedule){
        if(messageSchedule.id!=null){
            if(messageSchedule.own){
                updateMessageSchedule(messageSchedule)
            }else{
                deleteMessageSchedule(messageSchedule)
            }
        }else{
            if(messageSchedule.own){
                createMessageSchedule(messageSchedule)
            }
        }
    }

    @Transactional
    ImmediateMessage saveMessage(ImmediateMessage message){
        message.updateDate=new Date();
        if(message.content!=null){
            if(mediaContentService.isContentGenerated(message.content) && message.contentExt==null){
                GeneratedMediaContent gen=mediaContentService.generateMediaContent(message.content, Area.SCREEN);
                message.contentExt=gen;
            }
            if(!mediaContentService.isContentGenerated(message.content) && message.contentExt!=null){
                mediaContentService.deleteMediaContent(message.contentExt);
                message.contentExt=null;
            }
        }else{
            if(message.contentExt!=null){
                mediaContentService.deleteMediaContent(message.contentExt);
                message.contentExt=null;
            }
        }
         if(!message.id){
             return immediateMessageDao.create(message);
         }else{
             return immediateMessageDao.update(message);
         }
    }


    @Transactional
    List<ImmediateMessage> getImmediateMessages(ObjectType type, Long ownerId){
        immediateMessageDao.list(type, ownerId);
    }

    @Transactional
    ImmediateMessage getImmediateMessage(Long messageId){
        return immediateMessageDao.find(messageId);
    }

    @Transactional
    void deleteImmediateMessagesFor(Long messageId){
        immediateMessageDao.delete(messageId);
    }

    @Transactional
    void deleteImmediateMessages(ObjectType ownerType, Long ownerId){
        immediateMessageDao.deleteFor(ownerType, ownerId);
    }

    @Transactional
    ImmediateMessage getPlayingMessage(ObjectType ownerType, Long ownerId){
        def closure={ObjectType ot, Long oid -> immediateMessageDao.getCurrentMessage(ot, oid)}
        return retrieveResult(ownerType, ownerId, closure);
    }

    @Transactional
    boolean playMessage(Long messageId){
        ImmediateMessage imm=immediateMessageDao.find(messageId);
        ImmediateMessage playing=immediateMessageDao.getCurrentMessage(imm.ownerType, imm.ownerId);
        if(playing){
            return false;
        }
        immediateMessageDao.updateStatus(messageId, ImmMessageStatus.STARTED);
        return true;
    }

    @Transactional
    void stopMessage(Long messageId){
        immediateMessageDao.updateStatus(messageId, ImmMessageStatus.STOPPED);
        //TODO notify IMs
    }
}
