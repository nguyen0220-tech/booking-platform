package com.catholic.ac.kr.booking_platform.facility.core.provider.strategy_restaurant_menu;

import com.catholic.ac.kr.booking_platform.facility.constant.RestaurantMenuAct;
import com.catholic.ac.kr.booking_platform.facility.data.resraurant.Restaurant;
import com.catholic.ac.kr.booking_platform.facility.data.resraurant.RestaurantMenu;
import com.catholic.ac.kr.booking_platform.facility.data.resraurant.RestaurantMenuRepository;
import com.catholic.ac.kr.booking_platform.facility.dto.RestaurantMenuCommandRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public abstract class AbstractRestaurantMenuHandler implements RestaurantMenuHandler {
    private final RestaurantMenuRepository restaurantMenuRepository;

    @Override
    @Transactional
    public  ApiResponse<String> restaurantMenuHandle(Long userId, RestaurantMenuCommandRequest request) {
        RestaurantMenu menu = getRestaurantMenu(request.getMenuId());

        validateOwner(userId, menu);
        validateRestaurantState(menu.getRestaurant());

        return process(menu, request);
    }

    protected abstract ApiResponse<String> process(RestaurantMenu menu, RestaurantMenuCommandRequest request);

    protected void validateOwner(Long currentUserId, RestaurantMenu menu) {
        Long ownerId = menu.getRestaurant().getOwner().getId();

        if (!ownerId.equals(currentUserId)) {
            throw new AccessDeniedException("소유자가 아닙니다");

        }
    }

    protected void validateRestaurantState(Restaurant restaurant) {
        if (restaurant.isSuspended()) {
            throw new IllegalStateException("정지된 시설입니다");
        }
    }

    protected RestaurantMenu getRestaurantMenu(Long menuId) {
        return restaurantMenuRepository.findById(menuId)
                .orElseThrow(()-> new ResourceNotFoundException("menu not found"));
    }

    protected ApiResponse<String> buildResponseSuccess(RestaurantMenuAct act){
        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                act.getShowName()+" 되었습니다.");
    }
}
