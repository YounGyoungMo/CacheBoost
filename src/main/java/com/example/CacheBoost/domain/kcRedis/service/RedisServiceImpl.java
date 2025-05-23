package com.example.CacheBoost.domain.kcRedis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RedisServiceImpl implements RedisService {
    // Redis 연결 도구 → String은 key, Object는 value(뭐든 가능)
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void save(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public String get(String key) {
        // redis의 String자료형에 접근하는 메서드
        return (String)redisTemplate.opsForValue().get(key);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
