package com.fls.metro.app.security

import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.GenericFilterBean

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

import static com.fls.metro.core.security.TokenUtils.*

/**
 * User: NFadin
 * Date: 17.04.14
 * Time: 16:40
 */
@CompileStatic
@TupleConstructor
class AuthenticationTokenProcessingFilter extends GenericFilterBean {
    private UserDetailsService userDetailsService
    private Boolean validateToken

    @Override
    void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = getAsHttpRequest(request);
        String authToken = extractAuthTokenFromRequest(httpRequest)
        String username = getPrincipalNameFromToken(authToken)
        if (username) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username)
            if (!validateToken || validateToken(authToken, userDetails)) {
                UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities);
                authentication.details = new WebAuthenticationDetailsSource().buildDetails(httpRequest);
                SecurityContextHolder.context.authentication = authentication;
            }

        }

        chain.doFilter(request, response);
    }

    private static HttpServletRequest getAsHttpRequest(ServletRequest request) {
        if (!(request instanceof HttpServletRequest)) {
            throw new RuntimeException("Expecting an HTTP request");
        }
        return (HttpServletRequest) request;
    }


    private static String extractAuthTokenFromRequest(HttpServletRequest httpRequest) {
        String authToken = httpRequest.getHeader("X-Auth-Token");
        if (!authToken) {
            authToken = httpRequest.getParameter("token");
        }
        return authToken;
    }

    void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService
    }

    void setValidateToken(Boolean validateToken) {
        this.validateToken = validateToken
    }
}
