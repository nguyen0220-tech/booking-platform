package com.catholic.ac.kr.booking_platform.facility.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AddImagesForFacilityRequest {
    private Long facilityId;
    @NotEmpty(message = "입력 필수 항목입니다")
    private List<String> imageUrls = new ArrayList<>();
}
