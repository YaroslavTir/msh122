package com.fls.metro.api.resource.admin
import com.fls.metro.api.dto.admin.RemoveHierarchyObjectResponse
import com.fls.metro.core.data.domain.Im
import com.fls.metro.core.service.ImService
import com.fls.metro.core.service.NotificationService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.ws.rs.*
import javax.ws.rs.core.MediaType
/**
 * User: APermyakov
 * Date: 04.05.14
 * Time: 16:33
 */
@Path('im')
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
@Component
class ImResource extends AbstractResource {
    @Autowired
    private ImService imService
    @Autowired
    private NotificationService notificationService

    @GET
    public List<Im> get(@QueryParam('imName') String imName) {
        imService.nameStartsWith(imName)
    }

    @GET
    @Path('{id}')
    public Im get(@PathParam('id') Long id) {
        return imService.get(id, false)
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Im create(Im im) {
        imService.create(validate(im))
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Im update(Im im) {
        def res = imService.update(validate(im))
        notificationService.notifyIm(im.imName)
        res
    }

    @DELETE
    @Path('{id}')
    RemoveHierarchyObjectResponse delete(@PathParam('id') Long id) {
        new RemoveHierarchyObjectResponse(
                status: imService.delete(id)
        )
    }

}
