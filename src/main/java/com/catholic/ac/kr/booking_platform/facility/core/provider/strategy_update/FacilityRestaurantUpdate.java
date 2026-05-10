package com.catholic.ac.kr.booking_platform.facility.core.provider.strategy_update;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityType;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityRestaurantRepository;
import com.catholic.ac.kr.booking_platform.facility.data.Restaurant;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityInfoRestaurantUpdateUpdateRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class FacilityRestaurantUpdate extends AbstractFacilityUpdateHandler<Restaurant, FacilityInfoRestaurantUpdateUpdateRequest> {
    private final FacilityRestaurantRepository facilityRestaurantRepository;

    public FacilityRestaurantUpdate(FacilityRestaurantRepository facilityRestaurantRepository) {
        super();
        this.facilityRestaurantRepository = facilityRestaurantRepository;
    }

    @Override
    public FacilityType getFacilityType() {
        return FacilityType.RESTAURANT;
    }

    @Override
    public ApiResponse<String> updateFacility(Restaurant restaurant, FacilityInfoRestaurantUpdateUpdateRequest request) {
        boolean isFoodTypeNotUpdated = restaurant.getFoodType().equals(request.getFoodType());
        if (isFoodTypeNotUpdated && isNotUpdated(restaurant, request)) {
            System.out.println("Không có thay đổi, không query db RESTAURANT");
            return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                    "성공적으로 업데이트되었습니다");
        }

        updateFacilityInfo(restaurant, request);
        restaurant.updateFoodType(request.getFoodType());

        facilityRestaurantRepository.save(restaurant);

        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "성공적으로 업데이트되었습니다");
    }
}
