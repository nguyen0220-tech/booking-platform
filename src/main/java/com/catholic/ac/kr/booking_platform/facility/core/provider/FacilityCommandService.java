package com.catholic.ac.kr.booking_platform.facility.core.provider;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityType;
import com.catholic.ac.kr.booking_platform.facility.core.provider.strategy.FacilityHandler;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.ResourceNotFoundException;
import com.catholic.ac.kr.booking_platform.user.data.User;
import com.catholic.ac.kr.booking_platform.user.data.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacilityCommandService {
    private final UserRepository userRepository;

    private final Map<FacilityType, FacilityHandler<?>> facilityHandlers;

    public FacilityCommandService(UserRepository userRepository, List<FacilityHandler<?>> handlers) {
        this.userRepository = userRepository;
        this.facilityHandlers = handlers.stream()
                .collect(Collectors.toMap(FacilityHandler::getType, f -> f));
    }

    @PreAuthorize("hasRole('PROVIDER')")
    public ApiResponse<String> createFacility(Long ownerId, FacilityRequest request) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));

        FacilityHandler<?> handler = facilityHandlers.get(request.getType());
        return handler.create(owner, request);
    }
}
