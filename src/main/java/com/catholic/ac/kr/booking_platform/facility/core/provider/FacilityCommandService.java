package com.catholic.ac.kr.booking_platform.facility.core.provider;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityStatus;
import com.catholic.ac.kr.booking_platform.facility.constant.FacilityType;
import com.catholic.ac.kr.booking_platform.facility.core.provider.strategy.FacilityHandler;
import com.catholic.ac.kr.booking_platform.facility.data.Facility;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityRegistration;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityRegistrationRepository;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.BadRequestException;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.ResourceNotFoundException;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.UnsupportedStrategyException;
import com.catholic.ac.kr.booking_platform.user.data.User;
import com.catholic.ac.kr.booking_platform.user.data.UserRepository;
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
public class FacilityCommandService {
    private final UserRepository userRepository;
    private final Map<FacilityType, FacilityHandler<?>> facilityHandlers;
    private final FacilityRegistrationRepository facilityRegistrationRepository;

    public FacilityCommandService(UserRepository userRepository, List<FacilityHandler<?>> handlers, FacilityRegistrationRepository facilityRegistrationRepository) {

        this.userRepository = userRepository;
        this.facilityHandlers = handlers.stream()
                .collect(Collectors.toMap(FacilityHandler::getType, f -> f));
        this.facilityRegistrationRepository = facilityRegistrationRepository;
    }

    @PreAuthorize("hasRole('PROVIDER')")
    @CacheEvict(value = "facilityPage", allEntries = true)
    public ApiResponse<String> createFacility(Long ownerId, FacilityRequest request) {
        User owner = userRepository.getReferenceById(ownerId);

        FacilityHandler<?> handler = facilityHandlers.get(request.getType());
        if (handler == null) {
            throw new UnsupportedStrategyException(request.getType().name());
        }
        return handler.create(owner, request);
    }

    @Transactional
    @CacheEvict(value = "registrationStatus", allEntries = true)
    public ApiResponse<String> cancelFacility(Long ownerId, Long facilityId) {
        FacilityRegistration registration = facilityRegistrationRepository.findByFacilityIdWithFacility(facilityId)
                .orElseThrow(() -> new ResourceNotFoundException("registration not found"));

        Long facilityOwnerId = registration.getFacility().getOwner().getId();

        if (!facilityOwnerId.equals(ownerId)) {
            throw new AccessDeniedException("소유자가 아닙니다");
        }

        if (!FacilityStatus.PENDING.equals(registration.getStatus())) {
            throw new BadRequestException("이미 " + registration.getStatus().getDisplayStatus());
        }

        Facility facility = registration.getFacility();
        facility.setSuspended(true);
        facility.setActive(false);

        registration.setStatus(FacilityStatus.CANCELLED);
        registration.setNote("[소유자가 취소했습니다]");

        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "등록를 취소하셨습니다");
    }
}
