package com.catholic.ac.kr.booking_platform.facility.core;

import com.catholic.ac.kr.booking_platform.facility.data.FacilityImageRepository;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityImageDTO;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.infrastructure.components.UploadHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FacilityImageService {
    private final UploadHandler uploadHandler;
    private final FacilityImageRepository facilityImageRepository;

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
}
