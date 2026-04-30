package com.catholic.ac.kr.booking_platform.resolver;

import com.catholic.ac.kr.booking_platform.service.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class FacilityResolver {
    private final FacilityService facilityService;

}
