package com.fls.metro.core.security

import com.fls.metro.core.exception.WrongPrincipalException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails

/**
 * User: NFadin
 * Date: 27.05.2014
 * Time: 11:34
 */
class SecurityUtils {
    static String getUsername() throws WrongPrincipalException {
        def auth = SecurityContextHolder.context.authentication
        if (!auth) return null
        def principal = auth.principal
        if (principal instanceof String && principal == 'anonymousUser') {
            throw new WrongPrincipalException()
        }
        def userDetails = principal as UserDetails
        userDetails.username
    }
}
