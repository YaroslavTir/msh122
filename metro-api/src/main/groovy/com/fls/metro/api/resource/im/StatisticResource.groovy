package com.fls.metro.api.resource.im

import com.fls.metro.core.data.domain.statistic.ImStatistic
import com.fls.metro.core.security.SecurityUtils
import com.fls.metro.core.service.ImStatisticService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

/**
 * User: NFadin
 * Date: 02.06.2014
 * Time: 12:44
 */
@Slf4j
@Component
@Path('statistic')
@Produces(MediaType.APPLICATION_JSON)
class StatisticResource {

    @Autowired
    private ImStatisticService statisticService

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    void add(ImStatistic statistic) {
        def imName = SecurityUtils.username
        statistic.imName = imName
        statisticService.add(statistic)
    }
}
