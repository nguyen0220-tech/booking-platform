package com.catholic.ac.kr.booking_platform.facility.core.provider.strategy_restaurant_menu;

import com.catholic.ac.kr.booking_platform.facility.constant.RestaurantMenuAct;
import com.catholic.ac.kr.booking_platform.facility.data.resraurant.RestaurantMenu;
import com.catholic.ac.kr.booking_platform.facility.data.resraurant.RestaurantMenuRepository;
import com.catholic.ac.kr.booking_platform.facility.dto.RestaurantMenuCommandRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import org.springframework.stereotype.Component;

@Component
public class RestoreRestaurantMenuHandler extends AbstractRestaurantMenuHandler {
    public RestoreRestaurantMenuHandler(RestaurantMenuRepository restaurantMenuRepository) {
        super(restaurantMenuRepository);
    }

    @Override
    public RestaurantMenuAct getRestaurantMenuAct() {
        return RestaurantMenuAct.RESTORE;
    }

    @Override
    protected ApiResponse<String> process(RestaurantMenu menu, RestaurantMenuCommandRequest request) {
        if (!menu.isDeleted()) {
            return buildResponseSuccess(getRestaurantMenuAct());
        }
        menu.setDeleted(false);
        return buildResponseSuccess(getRestaurantMenuAct());
    }
}
