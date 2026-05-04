package com.catholic.ac.kr.booking_platform.profile.core.strategy;

import com.catholic.ac.kr.booking_platform.helper.HelperUtils;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.AlreadyExistsException;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.BadRequestException;
import com.catholic.ac.kr.booking_platform.profile.core.UpdateProfileCacheService;
import com.catholic.ac.kr.booking_platform.profile.core.UpdateProfileType;
import com.catholic.ac.kr.booking_platform.profile.core.event.UpdateProfileEvent;
import com.catholic.ac.kr.booking_platform.profile.data.PendingEmailUpdate;
import com.catholic.ac.kr.booking_platform.profile.data.UpdateProfileRequest;
import com.catholic.ac.kr.booking_platform.user.data.User;
import com.catholic.ac.kr.booking_platform.user.data.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class UpdateEmail implements ProfileStrategy{
    private final UserRepository userRepository;
    private final UpdateProfileCacheService updateProfileCacheService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public UpdateProfileType getUpdateProfileType() {
        return UpdateProfileType.EMAIL;
    }

    @Override
    public ApiResponse<String> updateProfile(User user, UpdateProfileRequest request) {
        if (HelperUtils.isInvalidEmail(request.getNewInfo())) {
            throw new BadRequestException("이메일 형식이 올바르지 않습니다");
        }
        String normalizeEmail = HelperUtils.normalizeEmail(request.getNewInfo());

        if (user.getEmail().equals(normalizeEmail)) {
            throw new BadRequestException("입력한 이메일 주소는 본 계정의 이메일입니다");
        }
        if (userRepository.existsByEmail(normalizeEmail)) {
            throw new AlreadyExistsException("이미 등록된 이메일입니다.");
        }
        return cacheEmail(user.getId(), normalizeEmail, user.getFullName());
    }


    private ApiResponse<String> cacheEmail(Long userId, String newEmail, String currentFullName) {
        if (updateProfileCacheService.existEmailCache(userId)) {
            String token = updateProfileCacheService.getTokenCache(userId);
            PendingEmailUpdate pendingEmailCache = updateProfileCacheService.getPendingEmailCache(token);

            log.info("Cache Pending Email: {}", pendingEmailCache);

            return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                    "이메일 변경 요청하셨습니다! 이메일 다시 확인하세요");
        }
        String token = UUID.randomUUID().toString();
        PendingEmailUpdate pendingEmail = new PendingEmailUpdate(userId, newEmail);
        updateProfileCacheService.saveEmailCache(
                token,
                pendingEmail);

        updateProfileCacheService.saveCacheLock(userId, token);

        log.info("New Pending Email: {}", pendingEmail);

        eventPublisher.publishEvent(new UpdateProfileEvent(currentFullName, newEmail, token)); //send email
        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "이메일 주소 변경을 인증하기 위한 이메일을 보냈습니다. 이메일을 확인하세요");
    }

}
