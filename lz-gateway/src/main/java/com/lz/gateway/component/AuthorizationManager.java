package com.lz.gateway.component;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.HttpMethod;
import com.lz.gateway.config.WhiteListConfig;
import com.lz.gateway.constants.SecurityConstants;
import com.nimbusds.jose.JWSObject;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 鉴权管理器
 *
 * @author liangzhong.tan
 * @version 1.1 2020-10-15
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    private final ReactiveRedisTemplate<String, Map<String, String>> redisTemplate;
    private final WhiteListConfig whiteListConfig;

    @SneakyThrows
    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
        ServerHttpRequest request = authorizationContext.getExchange().getRequest();
        String path = request.getURI().getPath();
        PathMatcher pathMatcher = new AntPathMatcher();

        //认证班名单处理
        whiteListConfig.getUrls().forEach(it -> {
            log.info("whiteList, path: {}, url: {}, is: {}", path, it, pathMatcher.match(it, path));
        });


        // token为空拒绝访问
        String token = request.getHeaders().getFirst(SecurityConstants.JWT_TOKEN_HEADER);
        if (StringUtils.isEmpty(token)) {
            return Mono.just(new AuthorizationDecision(false));
        }

        JWSObject jwsObject = JWSObject.parse(token);
        log.info("jws: {}", jwsObject);
        String payload = jwsObject.getPayload().toString();
        log.info("payload: {}", payload);

        return mono
//                .filter(Authentication::isAuthenticated)
                .flatMapIterable(it -> {
                    log.info("Authentication: {}", JSONObject.toJSONString(it));
                    return it.getAuthorities();
                })
                .map(it -> {
                    log.info("GrantedAuthority: {}", JSONObject.toJSONString(it));
                    return it.getAuthority();
                })
                .any(roleId -> {
                    // 从缓存取资源权限角色关系列表
                    Mono<Map<String, String>> mapMono = redisTemplate.opsForValue().get(SecurityConstants.RESOURCE_ROLES_KEY);

                    // 请求路径匹配到的资源需要的角色权限集合authorities统计
                    Set<String> authorities = new HashSet<>();
                    mapMono.subscribe(map -> map.forEach((k, v) -> {
                        if (pathMatcher.match(k, path)) {
                            authorities.addAll(Arrays.asList(v.split(",")));
                        }
                    }));

                    // roleId是请求用户的角色(格式:ROLE_{roleId})，authorities是请求资源所需要角色的集合
                    log.info("访问路径：{}", path);
                    log.info("用户角色信息：{}", roleId);
                    log.info("资源需要权限authorities：{}", authorities);
                    return authorities.contains(roleId);
                })
                .map(AuthorizationDecision::new)
                .defaultIfEmpty(new AuthorizationDecision(true));
    }
}
