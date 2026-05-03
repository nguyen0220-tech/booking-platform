package com.catholic.ac.kr.booking_platform.profile.core;

import com.catholic.ac.kr.booking_platform.profile.data.PendingEmailUpdate;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Service;

import java.util.Objects;
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

    private final Cache<Long, Integer> lockInputPassword = Caffeine.newBuilder()
            .expireAfterWrite(30, TimeUnit.MINUTES)
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

    /*
    Sử dụng asMap().compute để đảm bảo thread-safe

    Vấn đề: Đây gọi là lỗi Race Condition. Giả sử có 2 request (Luồng A và Luồng B) cùng gửi đến một lúc:
    A lấy được counter = 0.
    B cũng lấy được counter = 0 (vì A chưa kịp lưu giá trị mới).
    A tăng lên 1 và put(userId, 1).
    B cũng tăng lên 1 và put(userId, 1).
    Kết quả: User nhập sai 2 lần nhưng hệ thống chỉ đếm là 1.
     */
    public void verifyPasswordFail(Long userId) {
        lockInputPassword.asMap().compute(
                userId,
                (key, count) -> (count == null) ? 1 : count + 1);
    }

    public boolean isBlocked(Long userId) {
        final int TRY_MAX = 5;
        return Objects.requireNonNull(lockInputPassword.get(userId, k-> 0)) >= TRY_MAX;
    }

    public void verifyPasswordPassed(Long userId) {
        lockInputPassword.invalidate(userId);
    }
}
