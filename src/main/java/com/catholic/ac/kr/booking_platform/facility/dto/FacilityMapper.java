package com.catholic.ac.kr.booking_platform.facility.dto;

import com.catholic.ac.kr.booking_platform.facility.data.Facility;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityRegistration;
import com.catholic.ac.kr.booking_platform.facility.data.resraurant.RestaurantMenu;

public class FacilityMapper {

    public static FacilityDTO toFacilityDTO(Facility entity) {
        FacilityInfoDTO infos = getFacilityInfo(entity);

        return new FacilityDTO(
                entity.getId(),
                entity.getFacilityType().name(),
                entity.getOwner().getId(),
                infos
        );
    }

    public static FacilityDTO toFacilityDTO(FacilitySuggestionProjection projection){
        FacilityInfoDTO infos = new FacilityInfoDTO();
        infos.setName(projection.getName());
        infos.setAddress(projection.getAddress());
        infos.setAverageRating(projection.getAverageRating());
        infos.setTotalReviews(projection.getTotalReviews());


        FacilityDTO  facilityDTO = new FacilityDTO();
        facilityDTO.setId(projection.getId());
        facilityDTO.setFacilityType(projection.getFacilityType());
        facilityDTO.setFacilityInfo(infos);

        return facilityDTO;
    }

    public static FacilityDTO toFacilityDTO(FacilityRegistration registrationEntity) {
        Facility facility = registrationEntity.getFacility();
        FacilityInfoDTO infos = getFacilityInfo(facility);


        return new FacilityDTO(
                facility.getId(),
                facility.getFacilityType().name(),
                facility.getOwner().getId(),
                registrationEntity.getId(),
                infos
        );
    }

    private static FacilityInfoDTO getFacilityInfo(Facility entity) {
        FacilityInfoDTO infos = new FacilityInfoDTO();

        infos.setName(entity.getName());
        infos.setDescription(entity.getDescription());
        infos.setAddress(entity.getAddress());
        infos.setInstruction(entity.getInstruction() != null ? entity.getInstruction() : null);
        infos.setAverageRating(entity.getAverageRating());
        infos.setTotalReviews(entity.getTotalReviews());
        infos.setActive(entity.isActive());
        infos.setCarPark(entity.isCarPark());
        infos.setHasWifi(entity.isHasWifi());
        infos.setSuspended(entity.isSuspended());
        infos.setCreatedAt(entity.getCreatedAt());
        infos.setUpdatedAt(entity.getUpdatedAt());

        return infos;
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

    public static FacilityRegistrationDTO toFacilityRegistrationDTO(FacilityRegistration entity) {
        FacilityRegistrationDTO facilityRegistrationDTO = new FacilityRegistrationDTO();

        facilityRegistrationDTO.setId(entity.getId());
        facilityRegistrationDTO.setFacilityId(entity.getFacility().getId());
        facilityRegistrationDTO.setFacilityType(entity.getFacility().getFacilityType().name());
        facilityRegistrationDTO.setStatus(entity.getStatus().name());
        facilityRegistrationDTO.setNote(entity.getNote());
        facilityRegistrationDTO.setOwnerId(entity.getFacility().getOwner().getId());
        facilityRegistrationDTO.setReviewerId(entity.getReviewer().getId());
        facilityRegistrationDTO.setLastUpdateAt(entity.getLastUpdatedAt());

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
