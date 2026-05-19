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

    public FacilityDTO(Long id, Long ownerId){
        this.id = id;
        this.ownerId = ownerId;
    }

    public FacilityDTO(Long id, String facilityType, Long ownerId) {
        this.id = id;
        this.facilityType = facilityType;
        this.ownerId = ownerId;
    }
}
