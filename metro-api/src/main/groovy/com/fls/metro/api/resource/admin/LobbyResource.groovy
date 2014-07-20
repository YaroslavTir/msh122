package com.fls.metro.api.resource.admin

import com.fls.metro.api.dto.admin.RemoveHierarchyObjectResponse
import com.fls.metro.core.data.domain.Lobby
import com.fls.metro.core.service.LobbyService
import com.fls.metro.core.service.NotificationService
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
@Path("lobby")
@Slf4j
@Component
@Produces(MediaType.APPLICATION_JSON)
public class LobbyResource extends AbstractResource {

    @Autowired
    private LobbyService lobbyService;
    @Autowired
    private NotificationService notificationService

    @GET
    @Path('{id}')
    Lobby get(@PathParam('id') Long id) {
        lobbyService.get(id)
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    Lobby create(Lobby lobby) {
        return lobbyService.create(validate(lobby))
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    Lobby update(Lobby lobby) {
        def res = lobbyService.update(validate(lobby))
        notificationService.notifyLobby(lobby.id)
        res
    }

    @DELETE
    @Path('{id}')
    RemoveHierarchyObjectResponse delete(@PathParam('id') Long id) {
        new RemoveHierarchyObjectResponse(
                status: lobbyService.delete(id)
        )
    }
}
