package com.catholic.ac.kr.booking_platform.facility;

import com.catholic.ac.kr.booking_platform.facility.data.FacilityRegistration;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityRegistrationDTO;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityDTO;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityImageDTO;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityInfoDTO;
import com.catholic.ac.kr.booking_platform.facility.data.Facility;
import com.catholic.ac.kr.booking_platform.facility.projection.FacilityProjection;

public class FacilityMapper {
    public static FacilityDTO toFacilityDTO(FacilityProjection facilityProjection) {
        FacilityDTO facilityDTO = new FacilityDTO();
        facilityDTO.setId(facilityProjection.getId());
        facilityDTO.setFacilityType(facilityProjection.getFacilityType());
        facilityDTO.setOwnerId(facilityProjection.getOwnerId());

        return facilityDTO;
    }

    public static FacilityInfoDTO convertToFacilityInFfo(Facility facility) {
        FacilityInfoDTO facilityInfoDTO = new FacilityInfoDTO();

        facilityInfoDTO.setName(facility.getName());
        facilityInfoDTO.setDescription(facility.getDescription());
        facilityInfoDTO.setAddress(facility.getAddress());
        facilityInfoDTO.setInstruction(facility.getInstruction() != null ? facility.getInstruction() : null);
        facilityInfoDTO.setActive(facility.isActive());
        facilityInfoDTO.setCarPark(facility.isCarPark());
        facilityInfoDTO.setHasWifi(facility.isHasWifi());
        facilityInfoDTO.setSuspended(facility.isSuspended());
        facilityInfoDTO.setCreatedAt(facility.getCreatedAt());
        facilityInfoDTO.setUpdatedAt(facility.getUpdatedAt());

        return facilityInfoDTO;
    }

    public static String convertToFacilityImageUrl(FacilityImageDTO facilityImage){
        return facilityImage.getImageUrl();
    }

    public static FacilityRegistrationDTO convertToFacilityApprovalDTO(FacilityRegistration facilityRegistration) {
        FacilityRegistrationDTO facilityRegistrationDTO = new FacilityRegistrationDTO();

        facilityRegistrationDTO.setStatus(facilityRegistration.getStatus().getDisplayStatus());
        facilityRegistrationDTO.setNote(facilityRegistration.getNote());

        return facilityRegistrationDTO;
    }
}
