package com.catholic.ac.kr.booking_platform.infrastructure.security.userdetails;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.security.Principal;
import java.util.Objects;

public class SecurityUtils {
    private SecurityUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static UserDetailsImpl getUserDetails(Principal principal) {

        UserDetailsImpl userDetails;

        if (principal instanceof UsernamePasswordAuthenticationToken token
                && token.getPrincipal() instanceof UserDetailsImpl) {
            userDetails = (UserDetailsImpl) token.getPrincipal();

            return userDetails;
        }
        return null;
    }

    public static boolean isAdmin(Principal principal) {
        UserDetailsImpl userDetails;

        if (principal instanceof UsernamePasswordAuthenticationToken token
                && token.getPrincipal() instanceof UserDetailsImpl) {
            userDetails = (UserDetailsImpl) token.getPrincipal();

            return userDetails.getAuthorities().stream()
                    .anyMatch(authority
                            -> Objects.equals(authority.getAuthority(), "ROLE_ADMIN"));
        }
        return false;
    }

    public static boolean isAdmin(UserDetailsImpl userDetails) {
        if (userDetails != null) {
            return userDetails.getAuthorities().stream()
                    .anyMatch(a -> Objects.equals(a.getAuthority(), "ROLE_ADMIN"));

        } else
            return false;
    }

    public static boolean isProvider(Principal principal) {
        UserDetailsImpl userDetails;

        if (principal instanceof UsernamePasswordAuthenticationToken token
                && token.getPrincipal() instanceof UserDetailsImpl) {
            userDetails = (UserDetailsImpl) token.getPrincipal();

            return userDetails.getAuthorities().stream()
                    .anyMatch(authority
                            -> Objects.equals(authority.getAuthority(), "ROLE_PROVIDER"));
        }
        return false;
    }
}
