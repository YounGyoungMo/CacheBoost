package com.example.CacheBoost.common.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
public class CacheDebugLogger implements ApplicationRunner {
    private final CacheManager cacheManager;

    public CacheDebugLogger(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("CacheManager 사용 중: " + cacheManager.getClass().getName());
    }
}
