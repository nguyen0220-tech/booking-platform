package booking_platform.dto.response;

import booking_platform.dto.RoleDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class LoginResponse {
    private Long userId;
    private String fullName;
    private Set<RoleDTO> roles;
}
