package com.catholic.ac.kr.booking_platform.facility.core.provider.strategy_restaurant_menu;

import com.catholic.ac.kr.booking_platform.facility.constant.RestaurantMenuAct;
import com.catholic.ac.kr.booking_platform.facility.data.resraurant.RestaurantMenu;
import com.catholic.ac.kr.booking_platform.facility.data.resraurant.RestaurantMenuRepository;
import com.catholic.ac.kr.booking_platform.facility.dto.RestaurantMenuCommandRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import org.springframework.stereotype.Component;

@Component
public class SoldOutRestaurantMenuHandler extends AbstractRestaurantMenuHandler{
    public SoldOutRestaurantMenuHandler(RestaurantMenuRepository restaurantMenuRepository) {
        super(restaurantMenuRepository);
    }

    @Override
    public RestaurantMenuAct getRestaurantMenuAct() {
        return RestaurantMenuAct.SOLD_OUT;
    }

    @Override
    protected ApiResponse<String> process(RestaurantMenu menu, RestaurantMenuCommandRequest request) {
        if (menu.isDeleted()) {
            throw new IllegalStateException("삭제된 메뉴입니다");
        }

        if (menu.isSoldOut()) {
            return buildResponseSuccess(RestaurantMenuAct.SOLD_OUT);
        }

        menu.setSoldOut(true);

        return buildResponseSuccess(RestaurantMenuAct.SOLD_OUT);
    }
}
