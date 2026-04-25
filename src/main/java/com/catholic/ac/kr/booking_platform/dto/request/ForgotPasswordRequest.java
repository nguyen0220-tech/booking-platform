package com.catholic.ac.kr.booking_platform.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ForgotPasswordRequest {
    @NotBlank(message = "아이디를 입력하셔야 합니다")
    private String username;
}
