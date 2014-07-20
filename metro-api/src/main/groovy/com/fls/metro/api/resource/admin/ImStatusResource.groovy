package com.fls.metro.api.resource.admin

import com.fls.metro.core.service.ImStatus
import com.fls.metro.core.service.ImStatusService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path('im/status')
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
@Component
class ImStatusResource {
    @Autowired
    ImStatusService imStatusService

    @GET
    @Path('{imName}')
    public ImStatus info(@PathParam('imName') String imName) {
        log.info("Im status imName:{}", imName);
        imStatusService.check(imName)
    }
}
