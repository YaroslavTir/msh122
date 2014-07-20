package com.fls.metro.app.security

import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor
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

/**
 * User: NFadin
 * Date: 25.04.14
 * Time: 10:41
 */
@CompileStatic
@TupleConstructor
class AuthenticationSessionProcessingFilter extends GenericFilterBean {
    private UserDetailsService userDetailsService
    private String usernameSessionAttr

    @Override
    void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = getAsHttpRequest(request);
        String username = httpRequest.session.getAttribute(usernameSessionAttr)

        if (username) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username)
            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities);
            authentication.details = new WebAuthenticationDetailsSource().buildDetails(httpRequest);
            SecurityContextHolder.context.authentication = authentication;
        }

        chain.doFilter(request, response);
    }

    private static HttpServletRequest getAsHttpRequest(ServletRequest request) {
        if (!(request instanceof HttpServletRequest)) {
            throw new RuntimeException("Expecting an HTTP request");
        }
        return (HttpServletRequest) request;
    }

    void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService
    }

    void setUsernameSessionAttr(String usernameSessionAttr) {
        this.usernameSessionAttr = usernameSessionAttr
    }
}
