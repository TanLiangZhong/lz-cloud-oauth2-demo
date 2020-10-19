package com.lz.oauth2service.service;

import com.lz.oauth2service.constants.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import javax.sql.DataSource;

/**
 * 使用  Redis 缓存 ClientDetails
 *
 * @author Tan
 * @version 1.1, 2020/10/15 14:49
 * @see JdbcClientDetailsService
 */
@Slf4j
public class RedisClientDetailsService extends JdbcClientDetailsService {
    public RedisClientDetailsService(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    @Cacheable(value = SecurityConstants.OAUTH_CLIENT_CACHE, key = "#clientId", unless = "#result == null")
    public ClientDetails loadClientByClientId(String clientId) throws InvalidClientException {
        log.info("clientId: {}", clientId);
        BaseClientDetails details = (BaseClientDetails) super.loadClientByClientId(clientId);
        details.setClientSecret(SecurityConstants.BCRYPT + details.getClientSecret());
        return details;
    }
}
