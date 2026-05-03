package com.catholic.ac.kr.booking_platform.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ForgotUsernameRequest {
    @NotBlank(message = "이메일을 입력하셔야 합니다")
    @Email(message = "이메일 형식이 올바르지 않습니다")
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "이메일 형식이 올바르지 않습니다"
    )
    private String email;

    @NotBlank(message = "휴대폰을 입력하셔야 합니다")
    private String phone;
}
