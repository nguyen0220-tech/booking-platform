package com.catholic.ac.kr.booking_platform.facility.core;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityStatus;
import com.catholic.ac.kr.booking_platform.facility.data.*;
import com.catholic.ac.kr.booking_platform.facility.dto.AddImagesForFacilityRequest;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityImageDTO;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.infrastructure.components.UploadHandler;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.BadRequestException;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FacilityImageService {
    private final UploadHandler uploadHandler;
    private final FacilityImageRepository facilityImageRepository;
    private final FacilityRegistrationRepository facilityRegistrationRepository;

    @Cacheable(value = "facilityImageUrls", key = "#entityIds")
    public List<FacilityImageDTO> getFacilityImageByEntityIds(List<Long> entityIds) {
        return facilityImageRepository.findAllByEntityIdIdIn(entityIds);
    }

    public ApiResponse<List<String>> uploadFacilityImage(Long ownerId, List<MultipartFile> images) {
        List<String> rs = new ArrayList<>();

        for (MultipartFile file : images) {
            String imageUrl = uploadHandler.uploadFile(ownerId, file);
            rs.add(imageUrl);
        }
        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "success", rs);
    }

    @Transactional
    @PreAuthorize("hasRole('PROVIDER')")
    @CacheEvict(value = "facilityImageUrls", allEntries = true)
    public ApiResponse<String> addImagesForFacility(Long ownerId, AddImagesForFacilityRequest request) {
        FacilityRegistration registration = facilityRegistrationRepository
                .findByFacilityIdWithFacility(request.getFacilityId())
                .orElseThrow(() -> new ResourceNotFoundException("Facility/Registration not found"));

        Facility facility = registration.getFacility();

        if (!ownerId.equals(facility.getOwner().getId())) {
            throw new AccessDeniedException("권한이 없습니다");
        }

        FacilityStatus status = registration.getStatus();
        if (status == FacilityStatus.CANCELLED || status == FacilityStatus.REJECTED) {
            throw new BadRequestException("취소되거나 거절된 시설은 이미지를 등록할 수 없습니다");
        }

        List<FacilityImage> images = new ArrayList<>();
        for (String url : request.getImageUrls()){
            FacilityImage facilityImage = new FacilityImage();
            facilityImage.setImageUrl(url);
            facilityImage.setType(facility.getFacilityType()); // Đã có sẵn facility từ JOIN FETCH
            facilityImage.setEntityId(facility.getId());

            images.add(facilityImage);
        }

        facilityImageRepository.saveAll(images);

        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "이미지가 업데이트되었습니다");
    }
}
