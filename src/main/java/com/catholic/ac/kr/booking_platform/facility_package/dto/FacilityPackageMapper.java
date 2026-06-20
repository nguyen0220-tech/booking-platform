package com.catholic.ac.kr.booking_platform.facility_package.dto;

import com.catholic.ac.kr.booking_platform.facility_package.data.FacilityPackage;

public class FacilityPackageMapper {
    public static FacilityPackageDTO toFacilityPackageDTO(FacilityPackage entity) {
        FacilityPackageDTO facilityPackageDTO = new FacilityPackageDTO();
        facilityPackageDTO.setId(entity.getId());
        facilityPackageDTO.setFacilityType(entity.getFacilityType().name());

        FacilityPackageInfoDetails details = new FacilityPackageInfoDetails();
        details.setPackageName(entity.getPackageName());
        details.setNote(entity.getNote());
        details.setTotalCount(entity.getTotalCount());
        details.setPrice(entity.getPrice());
        details.setSalePrice(entity.getSalePrice());
        details.setActive(entity.isActive());

        facilityPackageDTO.setInfoDetails(details);
        return facilityPackageDTO;
    }

}
