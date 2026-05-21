package com.catholic.ac.kr.booking_platform.facility.constant;

import lombok.Getter;

@Getter
public enum RestaurantMenuAct {
    DELETE("삭제"),
    RESTORE("복구"),
    SOLD_OUT("품절"),
    AVAILABLE("품절 해제");

    private final String showName;

    RestaurantMenuAct(String name) {
        this.showName = name;
    }
}
