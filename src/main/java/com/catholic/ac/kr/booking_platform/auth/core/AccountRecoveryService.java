package com.catholic.ac.kr.booking_platform.auth.core;

import com.catholic.ac.kr.booking_platform.auth.core.event.ForgotPasswordEvent;
import com.catholic.ac.kr.booking_platform.auth.data.TokenVerify;
import com.catholic.ac.kr.booking_platform.auth.data.TokenVerifyRepository;
import com.catholic.ac.kr.booking_platform.auth.dto.ForgotPasswordRequest;
import com.catholic.ac.kr.booking_platform.auth.dto.ForgotUsernameRequest;
import com.catholic.ac.kr.booking_platform.auth.dto.NewPasswordRequest;
import com.catholic.ac.kr.booking_platform.helper.HelperUtils;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.BadRequestException;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.ResourceNotFoundException;
import com.catholic.ac.kr.booking_platform.user.data.User;
import com.catholic.ac.kr.booking_platform.user.data.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AccountRecoveryService {
    private final UserRepository userRepository;
    private final TokenVerifyRepository tokenVerifyRepository;
    private final TokenVerifyService tokenVerifyService;
    private final ApplicationEventPublisher eventPublisher;
    private final PasswordEncoder passwordEncoder;

    public ApiResponse<String> forgotUsername(ForgotUsernameRequest request) {
        String norEmail = HelperUtils.normalizeEmail(request.getEmail());
        User user = userRepository.findByEmail(norEmail)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾지 못했습니다"));

        if (!user.getPhone().equals(request.getPhone())) {
            throw new BadRequestException("입력하신 정보가 일치하지 않습니다.");
        }
        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "정상적으로 처리되었습니다", user.getUsername());

    }

    public ApiResponse<String> forgotPassword(ForgotPasswordRequest request) {
        String nolUsername = HelperUtils.normalizeUsername(request.getUsername());
        User user = userRepository.findByUsername(nolUsername)
                .orElseThrow(() -> new ResourceNotFoundException("사용자가 존재하면 이메일로 안내를 보내드립니다."));

        if (user.isBlocked() || !user.isEnabled()) {
            throw new LockedException("본 계정은 비정상적인상태입니다");
        }

        String token = tokenVerifyService.createToken(user, TokenType.VERIFY_FORGOT_PASSWORD);

        eventPublisher.publishEvent(new ForgotPasswordEvent(user.getFullName(), user.getEmail(), token));

        String emailEncoded = HelperUtils.encodeEmail(user.getEmail());

        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "비밀번호 변경릏 인증하기 위해 이메일 " + emailEncoded + "으로 보냈습니다");
    }

    @Transactional
    public ApiResponse<String> resetPassword(NewPasswordRequest request) {
        TokenVerify tokenVerify = tokenVerifyRepository
                .findByTokenAndType(request.getToken(), TokenType.VERIFY_FORGOT_PASSWORD);

        if (tokenVerify == null || tokenVerify.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("인증 기간이 만료되었습니다");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("입력하신 비밀번호가 확인되지 않았습니다.");
        }

        User user = tokenVerify.getUser();

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        tokenVerifyRepository.delete(tokenVerify);

        userRepository.save(user);

        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "비밀번호가 새로 변경되었습니다");
    }
}
