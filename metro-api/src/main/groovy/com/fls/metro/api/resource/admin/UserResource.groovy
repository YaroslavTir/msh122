package com.fls.metro.api.resource.admin

import com.fls.metro.api.dto.admin.RemoveUserResponse
import com.fls.metro.core.data.domain.User
import com.fls.metro.core.service.UserService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.ws.rs.Consumes
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType

/**
 * User: NFadin
 * Date: 17.04.14
 * Time: 10:34
 */
@Path('user')
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
@Component
class UserResource extends AbstractResource {

    @Autowired
    UserService userService

    @GET
    public List<User> users() {
        userService.list().collect {
            hidePassword(it)
        }
    }

    @GET
    @Path('{id}')
    public User user(@PathParam('id') Long id) {
        hidePassword(userService.find(id))
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public User create(User user) {
        user.roles = ['ADMIN']
        hidePassword(userService.create(validate(user)))
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public User update(User user) {
        hidePassword(userService.update(validate(user)))
    }

    @DELETE
    @Path('{id}')
    public RemoveUserResponse delete(@PathParam('id') Long id) {
        new RemoveUserResponse(userService.delete(id))
    }

    private static hidePassword(User user) {
        user.password = ''
        user
    }
}
