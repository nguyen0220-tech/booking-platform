package com.catholic.ac.kr.booking_platform.profile.core.strategy;

import com.catholic.ac.kr.booking_platform.helper.HelperUtils;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.AlreadyExistsException;
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
public class UpdatePhone implements ProfileStrategy {
    private final UserRepository userRepository;

    @Override
    public UpdateProfileType getUpdateProfileType() {
        return UpdateProfileType.PHONE;
    }

    //개인 프로젝트이기 때문에 휴대폰 변경이 간단하게 구현함. 실제는 이메일처럼 구현.
    @Override
    public ApiResponse<String> updateProfile(User user, UpdateProfileRequest request) {
        if (HelperUtils.isInvalidPhone(request.getNewInfo())) {
            throw new BadRequestException("휴대폰 형식이 올바르지 않습니다");
        }

        if (user.getPhone().equals(request.getNewInfo())) {
            throw new BadRequestException("입력한 휴대폰는 본 계정의 휴대폰입니다");
        }

        if (userRepository.existsByPhone(request.getNewInfo())) {
            throw new AlreadyExistsException("이미 등록된 휴대톤입니다.");
        }

        user.setPhone(request.getNewInfo());
        userRepository.save(user);
        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "휴대폰 변경이 되었습니다.");
    }
}
