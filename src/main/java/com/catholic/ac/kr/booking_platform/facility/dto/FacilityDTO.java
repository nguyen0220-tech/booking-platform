package com.catholic.ac.kr.booking_platform.facility.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FacilityDTO {
    private Long id;
    private String facilityType;
    private Long ownerId;
    private Long facilityRegistrationId;

    private FacilityInfoDTO facilityInfo;

    public FacilityDTO(Long id, String facilityType, Long ownerId,FacilityInfoDTO facilityInfo) {
        this.id = id;
        this.facilityType = facilityType;
        this.ownerId = ownerId;
        this.facilityInfo = facilityInfo;
    }
}
