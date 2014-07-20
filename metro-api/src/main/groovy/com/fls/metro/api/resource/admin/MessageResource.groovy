package com.fls.metro.api.resource.admin

import com.fls.metro.core.data.domain.ImmediateMessage
import com.fls.metro.core.data.domain.ObjectType
import com.fls.metro.core.data.dto.messages.ImmMessageStatus
import com.fls.metro.core.service.HierarchyService
import com.fls.metro.core.service.ImService
import com.fls.metro.core.service.MessageService
import com.fls.metro.core.service.NotificationService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.validation.ConstraintViolationException
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
/**
 * Created with IntelliJ IDEA.
 * User: PGrebenyuk
 * Date: 07.05.14
 * Time: 10:31
 * To change this template use File | Settings | File Templates.
 */
@Path('message')
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
@Component
class MessageResource extends AbstractResource{

    @Autowired
    MessageService messageService;

    @Autowired
    ImService imService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    HierarchyService hierarchyService;


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public ImmediateMessage save(ImmediateMessage message) {
        return messageService.saveMessage(message);
    }

    @GET
    @Path('{type}/{id}')
    @Consumes(MediaType.APPLICATION_JSON)
    public List<ImmediateMessage> list(@PathParam("type") ObjectType ownerType, @PathParam("id") Long ownerId) {
        return messageService.getImmediateMessages(ownerType, ownerId);
    }

    @GET
    @Path('{type}/{id}/playing')
    @Consumes(MediaType.APPLICATION_JSON)
    public ImmediateMessage getPlayingMessage(@PathParam("type") ObjectType ownerType, @PathParam("id") Long ownerId) {
        return messageService.getPlayingMessage(ownerType, ownerId);
    }



    @GET
    @Path('{id}')
    public ImmediateMessage info(@PathParam('id') Long id) {
        return messageService.getImmediateMessage(id);
    }


    private notify(ObjectType oType, Long id){
        if(oType==ObjectType.IM){
            String imName=imService.getImNameById(id)
            if(imName!=null){
                notificationService.notifyIm(imName)
            }
        }else if(oType==ObjectType.LOBBY){
            notificationService.notifyLobby(id)
        }else if(oType==ObjectType.STATION){
            notificationService.notifyStation(id)
        }else if(oType==ObjectType.LINE){
            notificationService.notifyLine(id)
        }else if(oType==ObjectType.SCHEMA){
            notificationService.notifySchema(id)
        }
    }

    @PUT
    @Path('{id}/play')
    public boolean run(@PathParam('id') Long id) {
        boolean play=messageService.playMessage(id);
        if(play){
            ImmediateMessage imm=messageService.getImmediateMessage(id)
            notify(imm.ownerType, imm.ownerId)
        }else{
            throw new ConstraintViolationException("Play not allowed")
        }

    }

    @PUT
    @Path('{id}/stop')
    public boolean stop(@PathParam('id') Long id) {
        ImmediateMessage imm=messageService.getImmediateMessage(id);
        if(imm!=null && imm.status==ImmMessageStatus.STARTED){
            messageService.stopMessage(id);
            notify(imm.ownerType, imm.ownerId)
        }
    }


    @DELETE
    @Path('{id}')
    public void delete(@PathParam('id') Long id) {
        ImmediateMessage imm=messageService.getImmediateMessage(id);
        if(imm!=null && imm.status==ImmMessageStatus.STARTED){
            messageService.stopMessage(id);
            notify(imm.ownerType, imm.ownerId)
        }
        messageService.deleteImmediateMessagesFor(id);
    }


}
