package com.catholic.ac.kr.booking_platform.facility.core.provider.strategy_restaurant_menu;

import com.catholic.ac.kr.booking_platform.facility.constant.RestaurantMenuAct;
import com.catholic.ac.kr.booking_platform.facility.dto.RestaurantMenuCommandRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;

public interface RestaurantMenuHandler {
    RestaurantMenuAct getRestaurantMenuAct();

    ApiResponse<String> restaurantMenuHandle(Long userId, RestaurantMenuCommandRequest request);
}
