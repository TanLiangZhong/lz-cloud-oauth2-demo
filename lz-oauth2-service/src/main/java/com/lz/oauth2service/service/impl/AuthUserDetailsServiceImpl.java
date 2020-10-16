package com.lz.oauth2service.service.impl;

import com.lz.oauth2service.constants.SecurityConstants;
import com.lz.oauth2service.domain.OauthUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 * Redis 缓存配置
 *
 * @author Tan
 * @version 1.1, 2020/10/13 23:16
 */
@Service
public class AuthUserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO
        return new OauthUser(1L, "admin", SecurityConstants.BCRYPT + "$2a$10$INzB1xNj83poqjplMxVVgOB9SEei.Dx5j5mvRZuWiDbcp2R/bcBne", true);
    }

}
