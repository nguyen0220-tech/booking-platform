package com.catholic.ac.kr.booking_platform.auth.core;

import lombok.Getter;

@Getter
public enum CheckType {
    USERNAME("아이디"),
    EMAIL("이메일"),
    PHONE("휴대폰");

    private final String message;

    CheckType(String message) {
        this.message = message;
    }

}
