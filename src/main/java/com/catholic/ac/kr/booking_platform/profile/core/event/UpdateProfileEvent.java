package com.catholic.ac.kr.booking_platform.profile.core.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UpdateProfileEvent {
    private String fullName;
    private String email;
    private String token;
}
