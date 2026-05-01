package com.catholic.ac.kr.booking_platform.enumdef;

import lombok.Getter;

@Getter
public enum FoodType {
    KOREAN_FOOD("한식"),
    JAPANESE_FOOD("일식"),
    CHINESE_FOOD("중식"),
    VIETNAMESE_FOOD("베트남 음식"),
    OTHER("기타");

    private final String displayName;

    FoodType(String displayName) {
        this.displayName = displayName;
    }

}
