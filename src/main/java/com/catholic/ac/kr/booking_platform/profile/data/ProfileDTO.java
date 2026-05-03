package com.catholic.ac.kr.booking_platform.profile.data;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProfileDTO {
    protected String fullName;
    protected String email;
    protected String phone;
    private String avatarUrl;
}
