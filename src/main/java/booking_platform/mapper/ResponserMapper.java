package booking_platform.mapper;

import booking_platform.dto.LoginResponse;
import booking_platform.dto.RoleDTO;
import booking_platform.security.userdetails.UserDetailsImpl;

import java.util.Set;
import java.util.stream.Collectors;

public class ResponserMapper {
    public static LoginResponse toLoginResponse(UserDetailsImpl userDetails) {
        LoginResponse loginResponse = new LoginResponse();

        if (userDetails == null) {
            return loginResponse;
        } else {
            loginResponse.setUserId(userDetails.getId());

            Set<RoleDTO> roleSet = userDetails.getAuthorities().stream()
                    .map(r -> new RoleDTO(r.getAuthority()))
                    .collect(Collectors.toSet());
            loginResponse.setRoles(roleSet);

            loginResponse.setFullName(userDetails.getFullName());
        }
        return loginResponse;
    }
}
