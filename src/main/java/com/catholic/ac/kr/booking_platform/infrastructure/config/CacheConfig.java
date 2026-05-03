package com.catholic.ac.kr.booking_platform.infrastructure.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        List<CaffeineCache> caches = Arrays.asList(
                buildCaffeineCache("userPage",1,100),
                buildCaffeineCache("userInfos", 10,500),
                buildCaffeineCache("userRoles", 10,500),
                buildCaffeineCache("profile", 5,100)
        );

        cacheManager.setCaches(caches);

        return cacheManager;
    }

    private CaffeineCache buildCaffeineCache(String cacheName, int ttlMinutes, int maxSize) {
        return new CaffeineCache(
                cacheName,
                Caffeine.newBuilder()
                        .expireAfterWrite(ttlMinutes, TimeUnit.MINUTES)
                        .maximumSize(maxSize)
                        .build()
        );
    }
}
