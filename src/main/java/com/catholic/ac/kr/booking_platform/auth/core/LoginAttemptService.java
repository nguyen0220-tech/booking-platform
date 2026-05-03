package com.catholic.ac.kr.booking_platform.auth.core;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptService {

    private final Cache<String, Integer> attemptCache = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();

    public void loginFailed(String key) {
        /*.
        int attempts = Objects.requireNonNull(attemptCache.get(key, k -> 0));
        attempts++;
        attemptCache.put(key, attempts);

         */
        attemptCache.asMap().compute(
                key,
                (username, count) -> (count == null ? 1 : count + 1));
    }

    public boolean isBlocked(String key) {
        final int MAX_ATTEMPT = 5;
        return Objects.requireNonNull(attemptCache.get(key, k -> 0)) >= MAX_ATTEMPT;
    }

    public void loginSuccess(String key) {
        attemptCache.invalidate(key);
    }
}
