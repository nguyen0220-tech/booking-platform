package com.catholic.ac.kr.booking_platform.auth.core;

import com.catholic.ac.kr.booking_platform.auth.dto.CheckInfoRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.BadRequestException;
import com.catholic.ac.kr.booking_platform.user.data.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class UserCheckInfoService {
    private final Map<CheckType, Function<String, ApiResponse<String>>> handlerMap = Map.of(
            CheckType.USERNAME, this::checkUserName,
            CheckType.EMAIL, this::checkEmail,
            CheckType.PHONE, this::checkPhone);

    private final UserRepository userRepository;

    public ApiResponse<String> checkExistInfo(CheckInfoRequest request) {
        String keyword = request.getKeyword().trim();
        return handlerMap.getOrDefault(request.getCheckType(), this::unsupported).apply(keyword);
    }

    private ApiResponse<String> checkUserName(String username) {
        if (!username.matches("^[a-zA-Z0-9]+$")) {
            throw new BadRequestException("아이디 형식이 올바르지 않습니다");
        }
        return buildResponse(userRepository.existsByUsername(username), CheckType.USERNAME);
    }

    private ApiResponse<String> checkEmail(String email) {
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new BadRequestException("이메일 형식이 올바르지 않습니다");
        }

        return buildResponse(userRepository.existsByEmail(email), CheckType.EMAIL);
    }

    private ApiResponse<String> checkPhone(String phone) {
        if (!phone.matches("^\\d{9,11}$")) {
            throw new BadRequestException("전화번호 형식이 올바르지 않습니다");
        }

        return buildResponse(userRepository.existsByPhone(phone), CheckType.PHONE);
    }

    private ApiResponse<String> unsupported(String keyword) {
        throw new BadRequestException("지원하지 않는 타입입니다");
    }

    private ApiResponse<String> buildResponse(boolean exists, CheckType type) {
        String message = exists ? "사용중인 " + type.getMessage() + "입니다" : "사용가능한 " + type.getMessage() + "입니다";

        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), message);
    }
}
