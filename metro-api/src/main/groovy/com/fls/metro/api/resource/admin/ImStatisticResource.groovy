package com.fls.metro.api.resource.admin

import com.fls.metro.api.dto.SimpleDateParam
import com.fls.metro.core.data.domain.statistic.ImStatistic
import com.fls.metro.core.data.dto.GroupPeriod
import com.fls.metro.core.data.dto.grid.data.ImStatisticData
import com.fls.metro.core.data.filter.ImStatisticFilter
import com.fls.metro.core.data.filter.Order
import com.fls.metro.core.service.ImStatisticService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.ws.rs.*
import javax.ws.rs.core.MediaType

/**
 * User: NFadin
 * Date: 23.05.14
 * Time: 17:59
 */
@Path('im-statistic')
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
@Component
class ImStatisticResource {

    @Autowired
    private ImStatisticService imStatisticService

    @GET
    ImStatisticData filter(@QueryParam('imName') String imName,
                           @QueryParam('groupPeriod') GroupPeriod groupPeriod,
                           @QueryParam('periodDateFrom') SimpleDateParam periodDateFrom,
                           @QueryParam('periodDateTo') SimpleDateParam periodDateTo) {
        imStatisticService.filter(new ImStatisticFilter(
                imName: imName,
                groupPeriod: groupPeriod,
                periodDateFrom: periodDateFrom?.date?.toDate(),
                periodDateTo: periodDateTo?.date?.plusDays(1)?.toDate()
        ));
    }

    @GET
    @Path('{id}')
    public ImStatistic get(@PathParam('id') Long id) {
        imStatisticService.get(id)
    }
}
