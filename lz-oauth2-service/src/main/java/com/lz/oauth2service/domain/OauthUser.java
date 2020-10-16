package com.lz.oauth2service.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * OauthUser
 *
 * @author Tan
 * @version 1.1, 2020/10/14 14:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OauthUser implements UserDetails {

    private Long id;

    private String username;

    private String password;

    private Boolean enabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return Collections.emptyList();
        return Arrays.asList(new SimpleGrantedAuthority("admin"),
                new SimpleGrantedAuthority("user"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
