package com.catholic.ac.kr.booking_platform.profile.data;

import com.catholic.ac.kr.booking_platform.profile.core.UpdateProfileType;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateProfileRequest {
    private UpdateProfileType type;
    private String newInfo;
    private String confirmPassword;
}
