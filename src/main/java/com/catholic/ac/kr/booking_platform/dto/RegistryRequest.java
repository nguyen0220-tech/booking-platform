package com.catholic.ac.kr.booking_platform.dto;

import com.catholic.ac.kr.booking_platform.enumdef.RoleName;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistryRequest {
    @NotBlank(message = "아이디를 입력하세요")
    private String username;

    @NotBlank(message = "비밀번호를 입력하세요")
    private String password;

    @NotEmpty
    @Pattern(regexp = "^[\\p{L} ]+$", message = "정확한 이름을 입력하세요")
    private String fullName;

    @NotBlank
    @Email(message = "정확한 이메일을 입력하세요")
    private String email;

    @Pattern(regexp = "^[0-9]{9,11}$", message = "휴대폰 번호는 9~11 숫자 입력하세요")
    private String phone;

    @NotNull
    private RoleName role;
}
