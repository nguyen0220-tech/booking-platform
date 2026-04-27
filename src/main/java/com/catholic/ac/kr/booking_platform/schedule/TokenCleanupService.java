package com.catholic.ac.kr.booking_platform.schedule;

import com.catholic.ac.kr.booking_platform.repository.TokenVerifyRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TokenCleanupService {
    private final TokenVerifyRepository tokenVerifyRepository;

    public TokenCleanupService(TokenVerifyRepository tokenVerifyRepository) {
        this.tokenVerifyRepository = tokenVerifyRepository;
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void cleanup() {
        LocalDateTime now = LocalDateTime.now();
        tokenVerifyRepository.deleteExpiredTokens(now);
    }
}
