package com.fls.metro.api.resource.admin

import com.fls.metro.core.data.domain.Im
import com.fls.metro.core.data.domain.Schema
import com.fls.metro.core.service.ImService
import com.fls.metro.core.service.NotificationService
import com.fls.metro.core.service.SchemaService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.ws.rs.*
import javax.ws.rs.core.MediaType

/**
 * User: NFadin
 * Date: 22.04.14
 * Time: 9:47
 */
@Path('schema')
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
@Component
class SchemaResource extends AbstractResource {
    @Autowired
    private SchemaService schemaService

    @Autowired
    private NotificationService notificationService

    @GET
    public Schema schemas() {
        schemaService.schemaDTO
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Schema update(Schema schema) {
        def res = schemaService.update(validate(schema))
        notificationService.notifySchema(schema.id)
        res
    }
}
