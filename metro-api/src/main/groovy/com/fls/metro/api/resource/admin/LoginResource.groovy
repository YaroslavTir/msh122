package com.fls.metro.api.resource.admin

import com.fls.metro.api.dto.admin.UserAuth
import com.fls.metro.core.security.UserDetailsUser
import com.fls.metro.core.service.UserService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession
import javax.ws.rs.FormParam
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType

/**
 * User: NFadin
 * Date: 22.04.14
 * Time: 9:47
 */
@Path('login')
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
@Component
class LoginResource {
    @Autowired
    UserService userService
    @Autowired
    @Qualifier('userAuthenticationManager')
    AuthenticationManager authenticationManager

    @Value('${common.usernameSessionAttr}')
    private String usernameSessionAttr

    @Path('login')
    @POST
    public UserAuth login(@FormParam('username') String username, @FormParam('password') String password, @Context HttpServletRequest request) {
        SecurityContextHolder.context.authentication =
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password))
        def userDetails = userService.loadUserByUsername(username)
        log.info('User {} logged in', username)
        request.session.setAttribute(usernameSessionAttr, userDetails.username)
        new UserAuth(userDetails.username, userDetails.roles)
    }

    @Path('logout')
    @POST
    public UserAuth logout(@Context HttpServletRequest request) {
        HttpSession session = request.session
        if (session != null) {
            log.debug("Invalidating session: " + session.id);
            session.invalidate();
        }
        SecurityContextHolder.context.authentication = null;
        SecurityContextHolder.clearContext()
        return new UserAuth()
    }

    @Path('info')
    @GET
    public UserAuth info() {
        def auth = SecurityContextHolder.context.authentication;
        if (!auth) return null
        def principal = auth.principal
        if (principal instanceof String && principal == 'anonymousUser') {
            return null
        }
        def userDetails = principal as UserDetailsUser
        new UserAuth(userDetails.username, userDetails.roles)
    }
}
