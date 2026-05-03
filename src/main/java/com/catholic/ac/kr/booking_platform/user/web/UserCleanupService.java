package com.catholic.ac.kr.booking_platform.user.web;

import com.catholic.ac.kr.booking_platform.user.data.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class UserCleanupService {
    private final UserRepository userRepository;

    public UserCleanupService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Scheduled(cron = "0 0 12,6 * * ?")
    public void cleanup() {
        LocalDateTime expiryTime = LocalDateTime.now().minusMinutes(15);

        int deleteCount = userRepository.deleteUnenabledUsersOlderThan(expiryTime);

        log.info("사용자 (unenabled) {}: 삭제되었습니다", deleteCount);
    }
}
