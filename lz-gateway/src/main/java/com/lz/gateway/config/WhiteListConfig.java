package com.lz.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

/**
 * 白名单配置
 *
 * @author liangzhong.tan
 * @version 1.1 2020-10-15
 */
@Data
@Configuration
@ConfigurationProperties("oauth.whitelist")
public class WhiteListConfig {

    private List<String> urls = Collections.emptyList();

}
