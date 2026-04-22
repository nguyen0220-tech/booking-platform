package com.catholic.ac.kr.booking_platform.dto.request;

import com.catholic.ac.kr.booking_platform.enumdef.UpdateProfileType;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateProfileRequest {
    private UpdateProfileType type;
    private String newInfo;
    private String confirmPassword;
}
