package com.catholic.ac.kr.booking_platform.facility.core.provider.strategy;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityType;
import com.catholic.ac.kr.booking_platform.facility.core.event.NewFacilityEvent;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityImageRepository;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityRepository;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityRestaurantRepository;
import com.catholic.ac.kr.booking_platform.facility.data.Restaurant;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityRequest;
import com.catholic.ac.kr.booking_platform.facility.dto.RestaurantDTO;
import com.catholic.ac.kr.booking_platform.facility.dto.RestaurantRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.user.data.User;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RestaurantFacilityHandler extends AbstractFacilityHandler<RestaurantDTO> {
    private final FacilityRestaurantRepository facilityRestaurantRepository;

    public RestaurantFacilityHandler(FacilityRepository f, FacilityImageRepository i, ApplicationEventPublisher e, FacilityRestaurantRepository facilityRestaurantRepository) {
        super(f, i, e);
        this.facilityRestaurantRepository = facilityRestaurantRepository;
    }

    @Override
    public FacilityType getType() {
        return FacilityType.RESTAURANT;
    }

    @Override
    public List<RestaurantDTO> getSpecificDTOs(List<Long> ids) {
        return ids != null ? facilityRestaurantRepository.findRestaurantDTOsByIds(ids) : List.of();
    }

    @Override
    public ApiResponse<String> create(User owner, FacilityRequest baseRequest) {
        RestaurantRequest request = (RestaurantRequest) baseRequest;
        Restaurant newRestaurant = new Restaurant();

        setBasicFacility(owner, newRestaurant, request);
        newRestaurant.setFoodType(request.getFoodType());

        facilityRepository.save(newRestaurant);
        saveFacilityImages(newRestaurant.getId(), request.getType(), request.getImages());
        eventPublisher.publishEvent(new NewFacilityEvent(newRestaurant));


        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                request.getName() + "등록 접수가 되었습니다");

    }
}
