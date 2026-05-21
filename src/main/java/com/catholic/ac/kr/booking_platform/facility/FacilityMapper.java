package com.catholic.ac.kr.booking_platform.facility;

import com.catholic.ac.kr.booking_platform.facility.data.Facility;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityRegistration;
import com.catholic.ac.kr.booking_platform.facility.data.resraurant.RestaurantMenu;
import com.catholic.ac.kr.booking_platform.facility.dto.*;
import com.catholic.ac.kr.booking_platform.facility.projection.FacilityAdminProjection;
import com.catholic.ac.kr.booking_platform.facility.projection.FacilityRegistrationProjection;
import com.catholic.ac.kr.booking_platform.facility.projection.FacilitySummaryProjection;

public class FacilityMapper {
    public static FacilityDTO toFacilityDTO(FacilitySummaryProjection facilitySummaryProjection) {

        return new FacilityDTO(
                facilitySummaryProjection.getId(),
                facilitySummaryProjection.getFacilityType(),
                facilitySummaryProjection.getOwnerId()
        );
    }

    public static FacilityDTO toFacilityDTO(FacilityAdminProjection facilityAdminProjection) {
        FacilityDTO facilityDTO = new FacilityDTO();
        facilityDTO.setId(facilityAdminProjection.getId());
        facilityDTO.setFacilityType(facilityAdminProjection.getFacilityType());
        facilityDTO.setOwnerId(facilityAdminProjection.getOwnerId());
        facilityDTO.setFacilityRegistrationId(facilityAdminProjection.getFacilityRegistrationId());
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

    public static String convertToFacilityImageUrl(FacilityImageDTO facilityImage) {
        return facilityImage.getImageUrl();
    }

    public static FacilityRegistrationStatusDTO convertToFacilityRegistrationDTO(FacilityRegistration facilityRegistration) {
        FacilityRegistrationStatusDTO facilityRegistrationStatusDTO = new FacilityRegistrationStatusDTO();

        facilityRegistrationStatusDTO.setStatus(facilityRegistration.getStatus().getDisplayStatus());
        facilityRegistrationStatusDTO.setNote(facilityRegistration.getNote());
        facilityRegistrationStatusDTO.setLastUpdateAt(facilityRegistration.getLastUpdatedAt());

        return facilityRegistrationStatusDTO;
    }

    public static FacilityRegistrationDTO convertToFacilityRegistrationDTO(FacilityRegistrationProjection projection) {
        FacilityRegistrationDTO facilityRegistrationDTO = new FacilityRegistrationDTO();

        facilityRegistrationDTO.setId(projection.getId());
        facilityRegistrationDTO.setFacilityId(projection.getFacilityId());
        facilityRegistrationDTO.setFacilityType(projection.getFacilityType());
        facilityRegistrationDTO.setStatus(projection.getStatus());
        facilityRegistrationDTO.setNote(projection.getNote());
        facilityRegistrationDTO.setOwnerId(projection.getOwnerId());
        facilityRegistrationDTO.setReviewerId(projection.getReviewerId());
        facilityRegistrationDTO.setLastUpdateAt(projection.getLastUpdateAt());

        return facilityRegistrationDTO;
    }

    public static RestaurantMenuDTO toRestaurantMenuDTO(RestaurantMenu facilityMenu) {
        RestaurantMenuDTO menuDTO = new RestaurantMenuDTO();

        menuDTO.setId(facilityMenu.getId());
        menuDTO.setName(facilityMenu.getName());
        menuDTO.setDescription(facilityMenu.getDescription());
        menuDTO.setPrice(facilityMenu.getPrice());
        menuDTO.setImageUrl(facilityMenu.getImageUrl());
        menuDTO.setDeleted(facilityMenu.isDeleted());
        menuDTO.setSoldOut(facilityMenu.isSoldOut());

        return menuDTO;
    }
}
