package com.fls.metro.api.resource.im

import com.fls.metro.core.exception.WrongPrincipalException
import com.fls.metro.core.security.SecurityUtils
import com.fls.metro.core.data.dto.content.Content
import com.fls.metro.core.service.ContentService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.MediaType

/**
 * User: NFadin
 * Date: 17.04.14
 * Time: 11:28
 */
@Slf4j
@Component
@Path('content')
@Produces(MediaType.APPLICATION_JSON)
class ContentResource {

    @Autowired
    ContentService contentService

    @GET
    List<Content> get() {
        try {
            contentService.get(SecurityUtils.username)
        } catch (WrongPrincipalException e) {
            log.error('Error wrong principal', e)
            throw new WebApplicationException(401)
        }
    }
}
