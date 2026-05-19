package com.catholic.ac.kr.booking_platform.facility_package.mapper;

import com.catholic.ac.kr.booking_platform.facility.dto.FacilityDTO;
import com.catholic.ac.kr.booking_platform.facility_package.dto.SportPackageDTO;
import com.catholic.ac.kr.booking_platform.facility_package.projection.SportPackageProjection;

public class SportPackageMapper {
    public static SportPackageDTO toSportPackageDTO(SportPackageProjection projection) {
        SportPackageDTO dto = new SportPackageDTO();

        dto.setId(projection.getId());
        dto.setStartTime(projection.getStartTime());
        dto.setEndTime(projection.getEndTime());
        dto.setTotalPrice(projection.getTotalPrice());

        if (projection.getFacilityId() != null && projection.getOwnerId() != null) {
            dto.setFacility( new FacilityDTO(
                    projection.getFacilityId(),
                    projection.getOwnerId()));
        }

        return dto;
    }
}
