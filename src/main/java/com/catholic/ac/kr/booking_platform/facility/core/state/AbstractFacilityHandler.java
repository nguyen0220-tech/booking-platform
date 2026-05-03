package com.catholic.ac.kr.booking_platform.facility.core.state;

import com.catholic.ac.kr.booking_platform.facility.dto.FacilityRequest;
import com.catholic.ac.kr.booking_platform.facility.constant.FacilityType;
import com.catholic.ac.kr.booking_platform.facility.data.Facility;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityImage;
import com.catholic.ac.kr.booking_platform.user.data.User;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityImageRepository;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

@RequiredArgsConstructor
public abstract class AbstractFacilityHandler implements FacilityHandler {
    protected final FacilityRepository facilityRepository;
    protected final FacilityImageRepository facilityImageRepository;
    protected final ApplicationEventPublisher eventPublisher;

    protected void setBasicFacility(User owner, Facility facility, FacilityRequest request) {
        facility.setOwner(owner);
        facility.setName(request.getName());
        facility.setDescription(request.getDescription());
        facility.setAddress(request.getAddress());
        facility.setInstruction(request.getInstruction());
        facility.setActive(request.isActive());
        facility.setCarPark(request.isCarPark());
        facility.setHasWifi(request.isHasWifi());
    }

    protected void saveFacilityImages(Long entityId, FacilityType type, List<String> images) {
        if (images == null || images.isEmpty()) return;
        List<FacilityImage> list = images.stream().map(url -> {
            FacilityImage img = new FacilityImage();
            img.setEntityId(entityId);
            img.setType(type);
            img.setImageUrl(url);
            return img;
        }).toList();
        facilityImageRepository.saveAll(list);
    }
}

