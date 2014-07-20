package com.fls.metro.api.resource.admin

import com.fls.metro.core.data.dto.grid.data.PassengerMessageData
import com.fls.metro.core.data.filter.Order
import com.fls.metro.core.data.filter.PassengerMessageFilter
import com.fls.metro.core.service.PassengerMessageService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType

/**
 * User: NFadin
 * Date: 23.05.14
 * Time: 17:59
 */
@Path('passenger-message')
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
@Component
class PassengerMessageResource {

    @Autowired
    private PassengerMessageService passengerMessageService

    @GET
    PassengerMessageData filter(@QueryParam('imName') String imName,
                                @QueryParam('createDate') Date createDate,
                                @QueryParam('currentPage') Integer currentPage,
                                @QueryParam('pageSize') Integer pageSize) {
        passengerMessageService.filter(new PassengerMessageFilter(
                imName: imName,
                currentPage: currentPage,
                pageSize: pageSize,
                createDate: createDate,
                orders: [new Order(
                        field: 'createDate',
                        direction: 'DESC'
                )]
        ));
    }
}
