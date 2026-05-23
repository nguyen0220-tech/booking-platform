package com.catholic.ac.kr.booking_platform.facility_package.core.strategy;

import com.catholic.ac.kr.booking_platform.facility.data.Facility;
import com.catholic.ac.kr.booking_platform.facility_package.data.FacilityPackage;
import com.catholic.ac.kr.booking_platform.facility_package.data.FacilityPackageRepository;
import com.catholic.ac.kr.booking_platform.facility_package.dto.FacilityPackageRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;

@RequiredArgsConstructor
public abstract class AbstractPackageHandler<T extends FacilityPackageRequest> implements FacilityPackageHandler<T> {
    protected final FacilityPackageRepository packageRepository;

    protected void setBasicPackage(FacilityPackage newPackage, FacilityPackageRequest packageRequest) {
        newPackage.setPackageName(packageRequest.getPackageName());
        newPackage.setNote(packageRequest.getNote());
        newPackage.setFacilityType(packageRequest.getFacilityType());
        newPackage.setSalePrice(packageRequest.getSalePrice());
    }

    protected void validateFacility(Long currentUserId, Facility facility) {
        if (!facility.getOwner().getId().equals(currentUserId)) {
            throw new AccessDeniedException("소유자가 아닙니다");
        }

        if (facility.isSuspended()) {
            throw new IllegalStateException("정지된 시설입니다");
        }
    }

    protected ApiResponse<String> buildResponseSuccess(String name) {
        return ApiResponse.success(HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase(),
                name + " 추가되었습니다");
    }
}
