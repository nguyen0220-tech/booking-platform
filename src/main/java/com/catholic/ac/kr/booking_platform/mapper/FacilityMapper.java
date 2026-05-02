package com.catholic.ac.kr.booking_platform.mapper;

import com.catholic.ac.kr.booking_platform.dto.*;
import com.catholic.ac.kr.booking_platform.dto.request.FacilityApprovalDTO;
import com.catholic.ac.kr.booking_platform.model.Facility;
import com.catholic.ac.kr.booking_platform.model.FacilityApproval;
import com.catholic.ac.kr.booking_platform.projection.FacilityProjection;

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
        facilityInfoDTO.setCreatedAt(facility.getCreatedAt());
        facilityInfoDTO.setUpdatedAt(facility.getUpdatedAt());

        return facilityInfoDTO;
    }

    public static String convertToFacilityImageUrl(FacilityImageDTO facilityImage){
        return facilityImage.getImageUrl();
    }

    public static FacilityApprovalDTO convertToFacilityApprovalDTO(FacilityApproval facilityApproval) {
        FacilityApprovalDTO facilityApprovalDTO = new FacilityApprovalDTO();

        facilityApprovalDTO.setStatus(facilityApproval.getStatus().getDisplayStatus());
        facilityApprovalDTO.setNote(facilityApproval.getNote());

        return facilityApprovalDTO;
    }
}
