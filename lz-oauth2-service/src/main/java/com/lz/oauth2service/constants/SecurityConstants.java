package com.lz.oauth2service.constants;

/**
 * 安全认证常量
 *
 * @author Tan
 * @version 1.1, 2020/10/13 20:46
 */
public interface SecurityConstants {

    /**
     * oauth2 相关前缀
     */
    String OAUTH_PREFIX = "oauth2:token:";

    /**
     * oauth2 客户端信息
     */
    String OAUTH_CLIENT_CACHE = "oauth2:client";

    /**
     * {bcrypt} 加密的特征码
     */
    String BCRYPT = "{bcrypt}";

    /**
     * jwt 密钥信息 key
     */
    String JWT_USER_ID_KEY = "id";

    /**
     * jwt 密钥信息 key
     */
    String JWT_USERNAME_KEY = "username";

    /**
     * key store 通行证
     */
    String KEY_STORE_PASS = "KEY_STORE_PASS";

    /**
     * key store 别名
     */
    String KEY_ALIAS = "KEY_ALIAS";

    /**
     * 认证信息Http请求头
     */
    String JWT_TOKEN_HEADER = "Authorization";

    /**
     * Redis缓存权限规则key
     */
    String RESOURCE_ROLES_KEY = "auth:resource:roles";

    /**
     * JWT存储权限前缀
     */
    String AUTHORITY_PREFIX = "ROLE_";

    /**
     * JWT存储权限属性
     */
    String AUTHORITY_CLAIM_NAME = "authorities";

    /**
     * JWT令牌前缀
     */
    String JWT_TOKEN_PREFIX = "Bearer ";

    /**
     * JWT载体key
     */
    String JWT_PAYLOAD_KEY = "payload";
}
