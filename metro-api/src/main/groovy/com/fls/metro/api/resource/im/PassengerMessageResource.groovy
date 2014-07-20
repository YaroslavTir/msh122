package com.fls.metro.api.resource.im

import com.fls.metro.core.data.domain.PassengerMessage
import com.fls.metro.core.exception.WrongPrincipalException
import com.fls.metro.core.service.ImService
import com.fls.metro.core.service.PassengerMessageService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.MediaType

/**
 * User: NFadin
 * Date: 27.05.2014
 * Time: 11:28
 */
@Slf4j
@Component
@Path('passenger-message')
@Produces(MediaType.APPLICATION_JSON)
class PassengerMessageResource {

    @Autowired
    private PassengerMessageService passengerMessageService
    @Autowired
    private ImService imService

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    void add(PassengerMessage message) {
        try {
            passengerMessageService.add(message)
        } catch (WrongPrincipalException e) {
            log.error('Error wrong principal', e)
            throw new WebApplicationException(401)
        }
    }
}
