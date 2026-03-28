package booking_platform.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserInfoDetailsDTO {
    private String username;
    private String fullName;
    private String phone;
    private String email;
    private String avatarUrl;
    private boolean enabled;
    private LocalDateTime createdAt;
}
