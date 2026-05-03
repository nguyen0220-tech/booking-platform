package com.catholic.ac.kr.booking_platform.auth.dto;

import com.catholic.ac.kr.booking_platform.auth.core.CheckType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckInfoRequest {
    private CheckType checkType;
    private String keyword;
}