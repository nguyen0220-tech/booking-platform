package com.catholic.ac.kr.booking_platform.auth;

import com.catholic.ac.kr.booking_platform.auth.dto.LoginResponse;
import com.catholic.ac.kr.booking_platform.user.dto.RoleDTO;
import com.catholic.ac.kr.booking_platform.infrastructure.security.userdetails.UserDetailsImpl;

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
