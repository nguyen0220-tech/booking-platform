package com.catholic.ac.kr.booking_platform.facility.core;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityType;
import com.catholic.ac.kr.booking_platform.facility.core.state.FacilityHandler;
import com.catholic.ac.kr.booking_platform.facility.core.state.MotelFacilityHandler;
import com.catholic.ac.kr.booking_platform.facility.core.state.RestaurantFacilityHandler;
import com.catholic.ac.kr.booking_platform.facility.core.state.SportFacilityHandler;
import com.catholic.ac.kr.booking_platform.facility.data.*;
import com.catholic.ac.kr.booking_platform.facility.dto.*;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.ResourceNotFoundException;
import com.catholic.ac.kr.booking_platform.user.data.User;
import com.catholic.ac.kr.booking_platform.user.data.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class FacilityCommandService {
    public final FacilityRepository facilityRepository;
    private final UserRepository userRepository;

    private final SportFacilityHandler sportFacilityHandler;
    private final MotelFacilityHandler motelFacilityHandler;
    private final RestaurantFacilityHandler restaurantFacilityHandler;

    private Map<FacilityType, FacilityHandler<?>> facilityHandlers;

    @PostConstruct
    private void init() {
        facilityHandlers = Map.of(
                FacilityType.SPORT, sportFacilityHandler,
                FacilityType.MOTEL, motelFacilityHandler,
                FacilityType.RESTAURANT, restaurantFacilityHandler
        );
    }

    @PreAuthorize("hasRole('PROVIDER')")
    public ApiResponse<String> createFacility(Long ownerId, FacilityRequest request) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));

        FacilityHandler<?> handler = facilityHandlers.get(request.getType());
        return handler.create(owner, request);
    }
}
