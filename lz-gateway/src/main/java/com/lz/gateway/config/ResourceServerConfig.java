package com.lz.gateway.config;

import com.lz.gateway.component.AuthorizationManager;
import com.lz.gateway.component.CustomServerAccessDeniedHandler;
import com.lz.gateway.component.CustomServerAuthenticationEntryPoint;
import com.lz.gateway.constants.SecurityConstants;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 资源服务器配置
 *
 * @author Tan
 * @version 1.1, 2020/10/16
 */
@AllArgsConstructor
@Configuration
@EnableWebFluxSecurity
public class ResourceServerConfig {

    private final AuthorizationManager authorizationManager;
    private final CustomServerAccessDeniedHandler customServerAccessDeniedHandler;
    private final CustomServerAuthenticationEntryPoint customServerAuthenticationEntryPoint;
    private final WhiteListConfig whiteListConfig;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http.oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(jwtAuthenticationConverter()).
                // 自定义处理JWT请求头过期或签名错误的结果
                and().authenticationEntryPoint(customServerAuthenticationEntryPoint);

        http.authorizeExchange()
                .pathMatchers(whiteListConfig.getUrls().toArray(new String[0])).permitAll()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .anyExchange().access(authorizationManager)
                .and()
                .exceptionHandling()
                // 访问拒绝, 无权访问
                .accessDeniedHandler(customServerAccessDeniedHandler)
                // 处理未认证, 未登录, token 无效
                .authenticationEntryPoint(customServerAuthenticationEntryPoint)
                .and().csrf().disable();
        return http.build();
    }

    /**
     * @return Converter
     * @link https://blog.csdn.net/qq_24230139/article/details/105091273 ServerHttpSecurity没有将jwt中authorities的负载部分当做Authentication
     * 需要把jwt的Claim中的authorities加入
     * 方案：重新定义ReactiveAuthenticationManager权限管理器，默认转换器 JwtGrantedAuthoritiesConverter
     */
    @Bean
    public Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix(SecurityConstants.AUTHORITY_PREFIX);
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName(SecurityConstants.AUTHORITY_CLAIM_NAME);

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }

}
