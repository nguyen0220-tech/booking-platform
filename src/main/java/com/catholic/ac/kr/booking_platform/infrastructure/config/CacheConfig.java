package com.catholic.ac.kr.booking_platform.infrastructure.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(500));

        return cacheManager;
    }

    /* .
    private CaffeineCache buildCaffeineCache(String cacheName, int ttlMinutes, int maxSize) {
        return new CaffeineCache(
                cacheName,
                Caffeine.newBuilder()
                        .expireAfterWrite(ttlMinutes, TimeUnit.MINUTES)
                        .maximumSize(maxSize)
                        .build()
        );
    }
     */
}
