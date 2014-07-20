package com.fls.metro.api.resource.admin

import com.fls.metro.core.data.domain.GeneratedMediaContent
import com.fls.metro.core.data.dto.content.media.MediaContentGenerateRequest
import com.fls.metro.core.service.MediaContentService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.ws.rs.*
import javax.ws.rs.core.MediaType

/**
 * User: NFadin
 * Date: 20.05.14
 * Time: 9:28
 */
@Path('mediacontent')
@Component
@Slf4j
@Produces(MediaType.APPLICATION_JSON)
class MediaContentResource extends AbstractResource {

    @Autowired
    private MediaContentService mediaContentService

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    GeneratedMediaContent create(MediaContentGenerateRequest request) {
        mediaContentService.generateMediaContent(request.content, request.area)
    }

    @DELETE
    @Path('{id}')
    GeneratedMediaContent delete(@PathParam("id") Long id) {
        mediaContentService.deleteMediaContent(id);
    }
}
