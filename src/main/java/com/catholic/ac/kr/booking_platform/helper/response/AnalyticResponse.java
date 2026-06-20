package com.catholic.ac.kr.booking_platform.helper.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnalyticResponse <T>{
    private T data;
}
