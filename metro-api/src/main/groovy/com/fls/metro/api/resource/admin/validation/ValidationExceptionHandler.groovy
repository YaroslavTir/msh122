package com.fls.metro.api.resource.admin.validation

import com.fls.metro.api.dto.Notification
import org.springframework.stereotype.Component
import org.springframework.web.bind.MethodArgumentNotValidException

import javax.validation.ConstraintViolationException
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

/**
 * User: NFadin
 * Date: 12.05.14
 * Time: 12:59
 */
@Provider
@Component
class ValidationExceptionHandler implements ExceptionMapper<ConstraintViolationException> {

    @Override
    Response toResponse(ConstraintViolationException e) {
        def result = new Notification()
        e.constraintViolations.each {
            result.errors << [(it.propertyPath.toString()): it.message]
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(result).build()
    }
}
