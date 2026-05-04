package com.catholic.ac.kr.booking_platform.profile.core.strategy;

import com.catholic.ac.kr.booking_platform.helper.HelperUtils;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.BadRequestException;
import com.catholic.ac.kr.booking_platform.profile.core.UpdateProfileType;
import com.catholic.ac.kr.booking_platform.profile.data.UpdateProfileRequest;
import com.catholic.ac.kr.booking_platform.user.data.User;
import com.catholic.ac.kr.booking_platform.user.data.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UpdateFullName implements ProfileStrategy {
    private final UserRepository userRepository;

    @Override
    public UpdateProfileType getUpdateProfileType() {
        return UpdateProfileType.FULL_NAME;
    }

    @Override
    public ApiResponse<String> updateProfile(User user, UpdateProfileRequest request) {
        String norNewName = HelperUtils.normalizeUsername(request.getNewInfo());

        if (user.getFullName().equals(norNewName)) {
            throw new BadRequestException("현재 본 계정 이릅이니다.");
        }
        user.setFullName(norNewName);
        userRepository.save(user);
        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "정보 변경이 되었습니다.");
    }
}
