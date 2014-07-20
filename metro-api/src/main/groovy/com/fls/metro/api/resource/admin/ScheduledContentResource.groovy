package com.fls.metro.api.resource.admin

import com.fls.metro.core.data.domain.ObjectType
import com.fls.metro.core.data.dto.schedule.ScheduledContent
import com.fls.metro.core.service.ScheduleService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired

import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path('schedule')
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
//@Component
class ScheduledContentResource {

    @Autowired
    ScheduleService scheduledContentService


    @GET
    @Path('{type}/{id}')
    public List<ScheduledContent> user(@PathParam('type') String type, @PathParam('id') Long id) {
        ObjectType ownerType=ObjectType.valueOf(type);
        scheduledContentService.getScheduleForEditing(ownerType, id);
    }

    @POST
    @Path('{type}/{id}')
    @Consumes(MediaType.APPLICATION_JSON)
    public List<ScheduledContent> create(@PathParam('type') String type, @PathParam('id') Long id, List<ScheduledContent> schedule) {
        ObjectType ownerType=ObjectType.valueOf(type);
        scheduledContentService.create(schedule, ownerType,id);
    }

    @PUT
    @Path('{type}/{id}')
    @Consumes(MediaType.APPLICATION_JSON)
    public List<ScheduledContent> update(@PathParam('type') String type, @PathParam('id') Long id, List<ScheduledContent> schedule) {
        ObjectType ownerType=ObjectType.valueOf(type);
        scheduledContentService.updateScheduleFor(schedule, ownerType, id);
    }

    @DELETE
    @Path('{type}/{id}')
    public void delete(@PathParam('type') String type, @PathParam('id') Long id) {
       ObjectType ownerType=ObjectType.valueOf(type);
       scheduledContentService.cleanScheduleFor(ownerType, id);
    }

}
