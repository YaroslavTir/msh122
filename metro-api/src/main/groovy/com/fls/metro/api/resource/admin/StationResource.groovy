package com.fls.metro.api.resource.admin

import com.fls.metro.api.dto.admin.RemoveHierarchyObjectResponse
import com.fls.metro.core.data.domain.Station
import com.fls.metro.core.service.NotificationService
import com.fls.metro.core.service.StationService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.ws.rs.*
import javax.ws.rs.core.MediaType

/**
 * User: APermyakov
 * Date: 05.05.14
 * Time: 16:28
 */
@Path("station")
@Slf4j
@Component
@Produces(MediaType.APPLICATION_JSON)
public class StationResource extends AbstractResource {

    @Autowired
    private StationService stationService;
    @Autowired
    private NotificationService notificationService

    @GET
    @Path('{id}')
    Station get(@PathParam('id') Long id) {
        stationService.get(id)
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    Station create(Station station) {
        return stationService.create(validate(station))
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    Station update(Station station) {
        def res = stationService.update(validate(station))
        notificationService.notifyStation(station.id)
        res
    }

    @DELETE
    @Path('{id}')
    RemoveHierarchyObjectResponse delete(@PathParam('id') Long id) {
        new RemoveHierarchyObjectResponse(
                status: stationService.delete(id)
        )
    }
}
