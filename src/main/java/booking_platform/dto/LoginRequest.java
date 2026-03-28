package booking_platform.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginRequest {
    @NotNull(message = "아이디를 입력하세요")
    private String username;

    @NotNull(message = "비밀번호를 입력하세요")
    private String password;
}
