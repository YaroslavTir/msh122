package com.fls.metro.core.security

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.crypto.password.StandardPasswordEncoder

/**
 * User: NFadin
 * Date: 25.04.14
 * Time: 18:20
 */
class ImPasswordEncoder implements PasswordEncoder {
    @Override
    String encode(CharSequence rawPassword) {
        return rawPassword
    }

    @Override
    boolean matches(CharSequence rawPassword, String encodedPassword) {
        return true
    }
}
