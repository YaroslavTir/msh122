package com.fls.metro.api.resource.admin

import com.fls.metro.core.build.BuildInfo
import com.fls.metro.core.build.BuildInfoUtils
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Component

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

/**
 * User: NFadin
 * Date: 22.05.14
 * Time: 11:00
 */
@Path('buildinfo')
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
@Component
class BuildInfoResource {

    @GET
    public BuildInfo get() {
        BuildInfoUtils.buildInfo
    }
}
