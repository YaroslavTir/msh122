package com.fls.metro.core.security

import com.fls.metro.core.data.domain.Role
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

/**
 * User: NFadin
 * Date: 16.04.14
 * Time: 14:51
 */
class UserDetailsUser implements UserDetails {

    private String username
    private String password
    private Set<GrantedAuthority> authorities
    private List<String> roles

    @Override
    Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities
    }

    @Override
    String getPassword() {
        return password
    }

    @Override
    String getUsername() {
        return username
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
