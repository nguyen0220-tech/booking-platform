package com.catholic.ac.kr.booking_platform.facility_package.web;

import com.catholic.ac.kr.booking_platform.facility.dto.FacilityDTO;
import com.catholic.ac.kr.booking_platform.facility.dto.SportDTO;
import com.catholic.ac.kr.booking_platform.facility_package.core.SportPackageService;
import com.catholic.ac.kr.booking_platform.facility_package.dto.SportPackageDTO;
import com.catholic.ac.kr.booking_platform.facility_package.mapper.SportPackageMapper;
import com.catholic.ac.kr.booking_platform.helper.response.ListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class SportPackageResolver {

    private final SportPackageService sportPackageService;

    @SchemaMapping(typeName = "Sport", field = "sportPackages")
    public ListResponse<SportPackageDTO> sportPackages(
            SportDTO sport,
            @Argument int page,
            @Argument int size) {
        return sportPackageService.getPackagesByFacilityId(sport.getId(), page, size);
    }
}
