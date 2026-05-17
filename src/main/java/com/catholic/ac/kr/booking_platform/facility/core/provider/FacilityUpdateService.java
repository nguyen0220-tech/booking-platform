package com.catholic.ac.kr.booking_platform.facility.core.provider;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityOption;
import com.catholic.ac.kr.booking_platform.facility.constant.FacilityStatus;
import com.catholic.ac.kr.booking_platform.facility.constant.FacilityType;
import com.catholic.ac.kr.booking_platform.facility.core.provider.strategy_option.FacilityOptionHandler;
import com.catholic.ac.kr.booking_platform.facility.core.provider.strategy_update.FacilityUpdateHandler;
import com.catholic.ac.kr.booking_platform.facility.data.Facility;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityRegistration;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityRegistrationRepository;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityInfoUpdateRequest;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityOptionRequest;
import com.catholic.ac.kr.booking_platform.facility.dto.OptionStateRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.BadRequestException;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.ResourceNotFoundException;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.UnsupportedStrategyException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacilityUpdateService {
    private final Map<FacilityOption, FacilityOptionHandler> optionHandlers;
    private final Map<FacilityType, FacilityUpdateHandler<? extends Facility, ? extends FacilityInfoUpdateRequest>> updateHandler;
    private final FacilityRegistrationRepository facilityRegistrationRepository;

    public FacilityUpdateService(List<FacilityOptionHandler> optionHandlers,
                                 List<FacilityUpdateHandler<? extends Facility, ? extends FacilityInfoUpdateRequest>> updateHandlers,
                                 FacilityRegistrationRepository facilityRegistrationRepository) {

        this.optionHandlers = optionHandlers.stream()
                .collect(Collectors.toMap(FacilityOptionHandler::getFacilityOption, o -> o));
        this.updateHandler = updateHandlers.stream()
                .collect(Collectors.toMap(FacilityUpdateHandler::getFacilityType, u -> u));
        this.facilityRegistrationRepository = facilityRegistrationRepository;
    }

    @PreAuthorize("hasRole('PROVIDER')")
    @Transactional
    @CacheEvict(value = "facilityInfos", allEntries = true)
    public ApiResponse<String> updateFacilityOption(Long ownerId, FacilityOptionRequest request) {
        if (request.getOptionStates().isEmpty()) {
            return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                    "성공적으로 설정되었습니다");
        }

        Facility facility = getFacility(request.getFacilityId());

        validationAccess(ownerId, facility);

        for (OptionStateRequest optionItem : request.getOptionStates()) {

            FacilityOptionHandler optionHandler = optionHandlers.get(optionItem.getOption());
            if (optionHandler == null) {
                throw new UnsupportedStrategyException(optionItem.getOption().name());
            }

            optionHandler.setFacilityOption(facility, optionItem);
        }

        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "성공적으로 설정되었습니다");
    }

    @PreAuthorize("hasRole('PROVIDER')")
    @Transactional
    @SuppressWarnings("unchecked")
    @CacheEvict(value = "facilityInfos", allEntries = true)
    public ApiResponse<String> updateFacilityInfo(Long ownerId, FacilityInfoUpdateRequest request) {
        if (request == null) {
            return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                    "성공적으로 설정되었습니다");
        }

        Facility facility = getFacility(request.getFacilityId());

        validationAccess(ownerId, facility);

        if (facility.getFacilityType() != request.getType()) {
            throw new IllegalArgumentException(
                    String.format("이 시설은 %s 입니다. %s 아닙니다",
                            facility.getFacilityType(), request.getType())
            );
        }

        FacilityUpdateHandler<Facility, FacilityInfoUpdateRequest> handler =
                (FacilityUpdateHandler<Facility, FacilityInfoUpdateRequest>) updateHandler.get(facility.getFacilityType());
        if (handler == null) {
            throw new UnsupportedStrategyException(facility.getFacilityType().name());
        }
        return handler.updateFacility(facility, request);
    }

    private void validationAccess(Long ownerId, Facility facility) {
        if (!ownerId.equals(facility.getOwner().getId())) {
            throw new AccessDeniedException("access denied");
        }
    }

    private Facility getFacility(Long facilityId) {

        FacilityRegistration facilityRegistration = facilityRegistrationRepository
                .findByFacilityIdWithFacility(facilityId)
                .orElseThrow(() -> new ResourceNotFoundException("facility registration not found"));

        FacilityStatus status = facilityRegistration.getStatus();
        if (status == FacilityStatus.CANCELLED || status == FacilityStatus.REJECTED) {
            throw new BadRequestException("취소되거나 거절된 시설은 정보 수정을 할 수 없습니다");
        }

        return facilityRegistration.getFacility();
    }
}
