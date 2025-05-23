package com.example.CacheBoost.common.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public CacheManager cacheManger() {
        return new ConcurrentMapCacheManager("searchHistories", "popularSearchKeywords", "searchBook");
    }

    /**
     * StringRedisTemplate는 문자열을 다루기 쉽게 해주는 템플릿
     * @param connectionFactory
     * @return
     */
    @Bean
    public StringRedisTemplate redisTemplate(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }


}
