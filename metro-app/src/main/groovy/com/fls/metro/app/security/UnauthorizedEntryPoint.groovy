package com.fls.metro.app.security

import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint

import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * User: NFadin
 * Date: 17.04.14
 * Time: 14:57
 */
class UnauthorizedEntryPoint implements AuthenticationEntryPoint {
    @Override
    void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                'Unauthorized')
    }
}
