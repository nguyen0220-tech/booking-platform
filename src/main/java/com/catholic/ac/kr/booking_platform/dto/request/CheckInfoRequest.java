package com.catholic.ac.kr.booking_platform.dto.request;

import com.catholic.ac.kr.booking_platform.enumdef.CheckType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckInfoRequest {
    private CheckType checkType;
    private String keyword;
}