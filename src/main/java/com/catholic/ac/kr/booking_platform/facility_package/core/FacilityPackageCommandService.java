package com.catholic.ac.kr.booking_platform.facility_package.core;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityType;
import com.catholic.ac.kr.booking_platform.facility.data.Facility;
import com.catholic.ac.kr.booking_platform.facility_package.constant.FacilityPackageAct;
import com.catholic.ac.kr.booking_platform.facility_package.core.strategy.FacilityPackageHandler;
import com.catholic.ac.kr.booking_platform.facility_package.data.FacilityPackage;
import com.catholic.ac.kr.booking_platform.facility_package.data.FacilityPackageRepository;
import com.catholic.ac.kr.booking_platform.facility_package.dto.FacilityPackageRequest;
import com.catholic.ac.kr.booking_platform.facility_package.dto.FacilityPackageUpdateRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.BadRequestException;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.ResourceNotFoundException;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.UnsupportedStrategyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacilityPackageCommandService {
    private final Map<FacilityType, FacilityPackageHandler> facilityPackageHandlers;
    private final FacilityPackageRepository facilityPackageRepository;

    public FacilityPackageCommandService(List<FacilityPackageHandler> handlers, FacilityPackageRepository facilityPackageRepository) {
        this.facilityPackageHandlers = handlers.stream()
                .collect(Collectors.toMap(
                        FacilityPackageHandler::getFacilityType,
                        fp -> fp
                ));
        this.facilityPackageRepository = facilityPackageRepository;
    }

    public ApiResponse<String> createNewPackage(Long userId, FacilityPackageRequest request) {
        FacilityPackageHandler handler = facilityPackageHandlers.get(request.getFacilityType());
        if (handler == null) {
            throw new UnsupportedStrategyException(request.getFacilityType().name());
        }

        return handler.createPackage(userId, request);
    }

    @Transactional
    public ApiResponse<String> updateStatusPackage(Long userId, FacilityPackageUpdateRequest request) {
        FacilityPackage facilityPackage = facilityPackageRepository.findByIdWithFacility(request.getPackageId())
                .orElseThrow(() -> new ResourceNotFoundException("Facility package not found"));

        if (!facilityPackage.getFacility().getOwner().getId().equals(userId)) {
            throw new AccessDeniedException("소유자가 아닙니다");
        }

        Facility facility = facilityPackage.getFacility();
        facility.validateFacility();

        FacilityPackageAct act = request.getAct();

        switch (act) {
            case ACTIVE -> {
                return activatePackage(facilityPackage, act);
            }
            case INACTIVE -> {
                return inactivatePackage(facilityPackage, act);
            }
            default -> throw new BadRequestException("유효하지 않은 메서드입니다. " + act.name());
        }
    }

    private ApiResponse<String> activatePackage(FacilityPackage facilityPackage, FacilityPackageAct act) {
        if (facilityPackage.isActive()) {
            return buildResponseSuccess(act);
        }
        facilityPackage.setActive(true);
        return buildResponseSuccess(act);
    }

    private ApiResponse<String> inactivatePackage(FacilityPackage facilityPackage, FacilityPackageAct act) {
        if (!facilityPackage.isActive()) {
            return buildResponseSuccess(act);
        }
        facilityPackage.setActive(false);
        return buildResponseSuccess(act);
    }

    private ApiResponse<String> buildResponseSuccess(FacilityPackageAct act) {
        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "패키지가 " + act.getDisplayName() + " 으로 설정되었습니다");
    }
}
