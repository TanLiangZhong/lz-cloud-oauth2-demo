package com.lz.gateway.filter;

import com.lz.gateway.constants.SecurityConstants;
import com.nimbusds.jose.JWSObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 黑名单token过滤器
 */
@Component
@Slf4j
public class AuthGlobalFilter implements GlobalFilter, Ordered {

//    @Autowired
//    private RedisTemplate redisTemplate;

    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst(SecurityConstants.JWT_TOKEN_HEADER);
        log.info("token: {}", token);
        if (StringUtils.isEmpty(token)) {
            return chain.filter(exchange);
        }
        token = token.replace(SecurityConstants.JWT_TOKEN_PREFIX, Strings.EMPTY);
        JWSObject jwsObject = JWSObject.parse(token);
        String payload = jwsObject.getPayload().toString();

        log.info("payload: {}", payload);

        // 黑名单token(登出、修改密码)校验
//        JSONObject jsonObject = JSONUtil.parseObj(payload);
//        String jti = jsonObject.getStr("jti"); // JWT唯一标识
//
//        Boolean isBlack = redisTemplate.hasKey(SecurityConstants.TOKEN_BLACKLIST_PREFIX + jti);
//        if (isBlack) {
//            ServerHttpResponse response = exchange.getResponse();
//            response.setStatusCode(HttpStatus.OK);
//            response.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
//            response.getHeaders().set("Access-Control-Allow-Origin", "*");
//            response.getHeaders().set("Cache-Control", "no-cache");
//            String body = JSONUtil.toJsonStr(Result.custom(ResultCode.TOKEN_INVALID_OR_EXPIRED));
//            DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
//            return response.writeWith(Mono.just(buffer));
//        }

        ServerHttpRequest request = exchange.getRequest().mutate()
                .header(SecurityConstants.JWT_PAYLOAD_KEY, payload)
                .build();
        exchange = exchange.mutate().request(request).build();
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
