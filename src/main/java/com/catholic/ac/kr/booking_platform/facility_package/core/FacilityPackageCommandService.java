package com.catholic.ac.kr.booking_platform.facility_package.core;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityType;
import com.catholic.ac.kr.booking_platform.facility_package.core.strategy.FacilityPackageHandler;
import com.catholic.ac.kr.booking_platform.facility_package.dto.FacilityPackageRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.UnsupportedStrategyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacilityPackageCommandService {
    private final Map<FacilityType, FacilityPackageHandler> facilityPackageHandlers;

    public FacilityPackageCommandService(List<FacilityPackageHandler> handlers) {
        this.facilityPackageHandlers = handlers.stream()
                .collect(Collectors.toMap(
                        FacilityPackageHandler::getFacilityType,
                        fp -> fp
                ));
    }

    public ApiResponse<String> createNewPackage(Long userId, Long facilityId, FacilityPackageRequest request){
        FacilityPackageHandler handler = facilityPackageHandlers.get(request.getFacilityType());
        if(handler == null){
            throw new UnsupportedStrategyException(request.getFacilityType().name());
        }

        return handler.createPackage(userId, facilityId, request);
    }
}
