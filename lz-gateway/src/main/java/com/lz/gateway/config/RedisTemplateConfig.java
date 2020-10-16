package com.lz.gateway.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * RedisTemplate  配置
 *
 * @author Tan
 * @version 1.1, 2020/10/15 15:00
 */
@EnableCaching
@Configuration
@AllArgsConstructor
@AutoConfigureBefore(RedisAutoConfiguration.class)
public class RedisTemplateConfig {
    private final ReactiveRedisConnectionFactory reactiveRedisConnectionFactory;

    @Bean
    public ReactiveRedisTemplate<String, Object> reactiveRedisTemplate() {
        /*
         * JdkSerializationRedisSerializer  序列化的Bean必须实现Serializable接口
         * GenericJackson2JsonRedisSerializer LocalDate 序列化方式无法识别
         * StringRedisSerializer 只能序列化字符串类型的数据
         * GenericFastJsonRedisSerializer 暂无问题
         */
        return new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory,
                RedisSerializationContext.<String, Object>newSerializationContext()
                        .key(new StringRedisSerializer())
                        .value(new GenericFastJsonRedisSerializer())
                        .hashKey(new StringRedisSerializer())
                        .hashValue(new GenericFastJsonRedisSerializer())
                        .build());
    }
}