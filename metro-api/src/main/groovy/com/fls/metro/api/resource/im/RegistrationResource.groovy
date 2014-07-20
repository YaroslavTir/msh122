package com.fls.metro.api.resource.im

import com.fls.metro.core.security.UserDetailsIm
import com.fls.metro.core.service.ImService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

import javax.servlet.http.HttpServletRequest
import javax.ws.rs.*
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType

import static com.fls.metro.core.security.TokenUtils.createImToken

/**
 * User: NFadin
 * Date: 24.04.14
 * Time: 17:41
 */
@Slf4j
@Component
@Path('registration')
class RegistrationResource {

    @Value('${common.realIpHeader}')
    private String realIpHeader

    @Autowired
    private ImService imService

    @Autowired
    @Qualifier('imAuthenticationManager')
    AuthenticationManager authenticationManager

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    String registration(@FormParam('name') String name, @Context HttpServletRequest request) {
        SecurityContextHolder.context.authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(name, ''))
        UserDetailsIm imDetails = imService.register(name, ip(request))
        log.info('Im {} registered', name)
        createImToken(imDetails)
    }

    private String ip(HttpServletRequest request) {
        if (realIpHeader) {
            log.info('Get ip from header')
            return request.getHeader(realIpHeader)
        }
        request.remoteAddr
    }
}
