package com.fls.metro.api.resource.admin

import com.fls.metro.api.dto.admin.RemoveHierarchyObjectResponse
import com.fls.metro.core.data.domain.Line;
import com.fls.metro.core.service.LineService
import com.fls.metro.core.service.NotificationService;
import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * User: APermyakov
 * Date: 05.05.14
 * Time: 16:28
 */
@Path("line")
@Slf4j
@Component
@Produces(MediaType.APPLICATION_JSON)
public class LineResource extends AbstractResource {

    @Autowired
    private LineService lineService;
    @Autowired
    private NotificationService notificationService

    @GET
    @Path('{id}')
    public Line get(@PathParam('id') Long id) {
        lineService.get(id)
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Line create(Line line) {
        return lineService.createLine(validate(line))
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Line update(Line line) {
        def res = lineService.updateLine(validate(line))
        notificationService.notifyLine(line.id)
        res
    }

    @DELETE
    @Path('{id}')
    RemoveHierarchyObjectResponse delete(@PathParam('id') Long id) {
        new RemoveHierarchyObjectResponse(
                status: lineService.delete(id)
        )
    }
}
