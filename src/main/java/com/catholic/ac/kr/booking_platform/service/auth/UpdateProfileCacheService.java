package com.catholic.ac.kr.booking_platform.service.auth;

import com.catholic.ac.kr.booking_platform.dto.PendingEmailUpdate;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class UpdateProfileCacheService {
    // token:(userId,newEmail)를 임시 저장
    private final Cache<String, PendingEmailUpdate> emailCache = Caffeine.newBuilder()
            .expireAfterWrite(15, TimeUnit.MINUTES)
            .maximumSize(1000)
            .build();

    // 여러번 변경 요청 차단을 위한 cache
    private final Cache<Long, String> lockCache = Caffeine.newBuilder()
            .expireAfterWrite(15, TimeUnit.MINUTES)
            .maximumSize(1000)
            .build();

    public boolean existEmailCache(Long userId) {
        return lockCache.getIfPresent(userId) != null;
    }

    public PendingEmailUpdate getPendingEmailCache(String token) {
        return emailCache.getIfPresent(token);
    }

    public void saveEmailCache(String token, PendingEmailUpdate pendingData) {
        emailCache.put(token, pendingData);
    }

    public void invalidateEmailCache(String token) {
        emailCache.invalidate(token);
    }

    public String getTokenCache(Long userId) {
        return lockCache.getIfPresent(userId);
    }

    public void saveCacheLock(Long userId, String token) {
        lockCache.put(userId, token);
    }
}
