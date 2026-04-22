package com.catholic.ac.kr.booking_platform.service.auth;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptService {
    private final int MAX_ATTEMPT = 5;

    private final Cache<String, Integer> attemptCache = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();

    public void loginFailed(String key) {
        int attempts = Objects.requireNonNull(attemptCache.get(key, k -> 0));

        attempts++;
        attemptCache.put(key, attempts);
    }

    public boolean isBlocked(String key) {
        return Objects.requireNonNull(attemptCache.get(key, k -> 0)) >= MAX_ATTEMPT;
    }

    public void loginSuccess(String key) {
        attemptCache.invalidate(key);
    }
}
