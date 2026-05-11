package com.catholic.ac.kr.booking_platform.facility.core;

import com.catholic.ac.kr.booking_platform.facility.data.Facility;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityImage;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityImageRepository;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityRepository;
import com.catholic.ac.kr.booking_platform.facility.dto.AddImagesForFacilityRequest;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityImageDTO;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.infrastructure.components.UploadHandler;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FacilityImageService {
    private final UploadHandler uploadHandler;
    private final FacilityImageRepository facilityImageRepository;
    private final FacilityRepository facilityRepository;

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

    @PreAuthorize("hasRole('PROVIDER')")
    public ApiResponse<String> addImagesForFacility(Long ownerId, AddImagesForFacilityRequest request) {
        Facility facility = facilityRepository.findById(request.getFacilityId())
                .orElseThrow(() -> new ResourceNotFoundException("Facility not found"));
        if (!ownerId.equals(facility.getOwner().getId())) {
            throw new AccessDeniedException("access denied");
        }

        List<FacilityImage> images = new ArrayList<>();

        for (String url: request.getImageUrls()){
            FacilityImage facilityImage = new FacilityImage();
            facilityImage.setImageUrl(url);
            facilityImage.setType(facility.getFacilityType());
            facilityImage.setEntityId(facility.getId());

            images.add(facilityImage);
        }

        facilityImageRepository.saveAll(images);

        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "이미지 업데이트되었습니다");
    }
}
