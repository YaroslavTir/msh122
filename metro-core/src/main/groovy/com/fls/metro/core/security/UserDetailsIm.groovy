package com.fls.metro.core.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

/**
 * User: NFadin
 * Date: 25.04.14
 * Time: 10:15
 */
class UserDetailsIm implements UserDetails {

    private String imName
    private Set<GrantedAuthority> authorities

    @Override
    Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities
    }

    @Override
    String getPassword() {
        return ''
    }

    @Override
    String getUsername() {
        return imName
    }

    @Override
    boolean isAccountNonExpired() {
        return true
    }

    @Override
    boolean isAccountNonLocked() {
        return true
    }

    @Override
    boolean isCredentialsNonExpired() {
        return true
    }

    @Override
    boolean isEnabled() {
        return true
    }
}
