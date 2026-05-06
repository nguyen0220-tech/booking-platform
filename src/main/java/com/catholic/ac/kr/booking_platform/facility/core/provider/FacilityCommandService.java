package com.catholic.ac.kr.booking_platform.facility.core.provider;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityOption;
import com.catholic.ac.kr.booking_platform.facility.constant.FacilityType;
import com.catholic.ac.kr.booking_platform.facility.core.provider.strategy.FacilityHandler;
import com.catholic.ac.kr.booking_platform.facility.core.provider.strategy_option.FacilityOptionHandler;
import com.catholic.ac.kr.booking_platform.facility.data.Facility;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityRepository;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityOptionRequest;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityRequest;
import com.catholic.ac.kr.booking_platform.facility.dto.OptionStateRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.ResourceNotFoundException;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.UnsupportedOptionException;
import com.catholic.ac.kr.booking_platform.user.data.User;
import com.catholic.ac.kr.booking_platform.user.data.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacilityCommandService {
    private final UserRepository userRepository;

    private final Map<FacilityType, FacilityHandler<?>> facilityHandlers;
    private final FacilityRepository facilityRepository;
    private final Map<FacilityOption, FacilityOptionHandler> optionHandlers;

    public FacilityCommandService(UserRepository userRepository, List<FacilityHandler<?>> handlers,
                                  FacilityRepository facilityRepository, List<FacilityOptionHandler> optionHandlers) {

        this.userRepository = userRepository;
        this.facilityHandlers = handlers.stream()
                .collect(Collectors.toMap(FacilityHandler::getType, f -> f));
        this.facilityRepository = facilityRepository;
        this.optionHandlers = optionHandlers.stream()
                .collect(Collectors.toMap(FacilityOptionHandler::getFacilityOption, o -> o));
    }

    @PreAuthorize("hasRole('PROVIDER')")
    public ApiResponse<String> createFacility(Long ownerId, FacilityRequest request) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));

        FacilityHandler<?> handler = facilityHandlers.get(request.getType());
        if (handler == null) {
            throw new UnsupportedOptionException(request.getType().name());
        }
        return handler.create(owner, request);
    }

    @Transactional
    public ApiResponse<String> updateFacilityOption(Long ownerId, FacilityOptionRequest request) {
        if (request.getOptionStates().isEmpty()) {
            return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                    "성공적으로 설정되었습니다");
        }
        Facility facility = facilityRepository.findById(request.getFacilityId())
                .orElseThrow(() -> new ResourceNotFoundException("facility not found"));

        if (!ownerId.equals(facility.getOwner().getId())) {
            throw new AccessDeniedException("access denied");
        }

        for (OptionStateRequest optionItem : request.getOptionStates()) {

            FacilityOptionHandler optionHandler = optionHandlers.get(optionItem.getOption());
            if (optionHandler == null) {
                throw new UnsupportedOptionException(optionItem.getOption().name());
            }

            optionHandler.setFacilityOption(facility, optionItem);
        }
        facilityRepository.save(facility);

        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "성공적으로 설정되었습니다");
    }
}
