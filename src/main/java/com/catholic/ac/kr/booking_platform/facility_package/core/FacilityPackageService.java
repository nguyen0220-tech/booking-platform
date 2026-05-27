package com.catholic.ac.kr.booking_platform.facility_package.core;

import com.catholic.ac.kr.booking_platform.facility_package.data.*;
import com.catholic.ac.kr.booking_platform.facility_package.dto.*;
import com.catholic.ac.kr.booking_platform.facility_package.mapper.FacilityPackageMapper;
import com.catholic.ac.kr.booking_platform.helper.response.ListResponse;
import com.catholic.ac.kr.booking_platform.helper.response.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacilityPackageService {
    private final FacilityPackageRepository facilityPackageRepository;
    private final SportPackageRepository sportPackageRepository;
    private final MotelPackageRepository motelPackageRepository;
    private final RestaurantPackageRepository restaurantPackageRepository;

    public ListResponse<FacilityPackageDTO> getFacilityPackages( Long facilityId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("totalCount").descending());

        Page<FacilityPackage> projections = facilityPackageRepository.findByFacilityId(facilityId, pageable);

        Page<FacilityPackageDTO> packageDTOPage = projections.map(FacilityPackageMapper::toFacilityPackageDTO);

        List<FacilityPackageDTO> rs = packageDTOPage.getContent();

        return new ListResponse<>(rs, new PageInfo(page, size, projections.hasNext()));
    }

    public List<SportPackageDTO> getSportPackages(List<Long> packageIds) {
        return sportPackageRepository.findAllByIds(packageIds);
    }

    public List<MotelPackageDTO> getMotelPackages(List<Long> packageIds) {
        return motelPackageRepository.findAllByIds(packageIds);
    }

    public List<RestaurantPackageDTO> getRestaurantPackages(List<Long> packageIds) {
        return restaurantPackageRepository.findAllByIds(packageIds);
    }

    public List<RestaurantPackageMenuDTO> getRestaurantPackageMenus(List<Long> packageIds) {
        return restaurantPackageRepository.findAllByPackageId(packageIds);
    }
}
