package com.catholic.ac.kr.booking_platform.facility.dto;

import com.catholic.ac.kr.booking_platform.facility.constant.RestaurantMenuAct;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantMenuCommandRequest {
    private Long menuId;
    private RestaurantMenuAct act;
}
