package com.catholic.ac.kr.booking_platform.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewPasswordRequest {
    private String token;
    @NotBlank(message = "새로 비밀번호를 입력하셔야 합니다")
    private String newPassword;
    @NotBlank(message = "새로 비밀번호 확인를 입력하셔야 합니다")
    private String confirmPassword;
}
