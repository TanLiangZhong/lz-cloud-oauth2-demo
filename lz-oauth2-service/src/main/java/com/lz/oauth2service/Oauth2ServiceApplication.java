package com.lz.oauth2service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Oauth2 认证服务
 *
 * @author liangzhong
 */
@EnableCaching
@SpringBootApplication
public class Oauth2ServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(Oauth2ServiceApplication.class, args);
    }

}
