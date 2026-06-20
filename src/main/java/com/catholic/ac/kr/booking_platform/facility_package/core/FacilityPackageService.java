package com.catholic.ac.kr.booking_platform.facility_package.core;

import com.catholic.ac.kr.booking_platform.facility.data.FacilityRepository;
import com.catholic.ac.kr.booking_platform.facility_package.data.*;
import com.catholic.ac.kr.booking_platform.facility_package.dto.*;
import com.catholic.ac.kr.booking_platform.facility_package.dto.FacilityPackageMapper;
import com.catholic.ac.kr.booking_platform.helper.response.ListResponse;
import com.catholic.ac.kr.booking_platform.helper.response.PageInfo;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.ResourceNotFoundException;
import com.catholic.ac.kr.booking_platform.infrastructure.security.userdetails.SecurityUtils;
import com.catholic.ac.kr.booking_platform.infrastructure.security.userdetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacilityPackageService {
    private final FacilityPackageRepository facilityPackageRepository;
    private final SportPackageRepository sportPackageRepository;
    private final MotelPackageRepository motelPackageRepository;
    private final RestaurantPackageRepository restaurantPackageRepository;
    private final FacilityRepository facilityRepository;

    public FacilityPackageDTO getFacilityPackage(Long id) {
        FacilityPackage facilityPackage = facilityPackageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Facility package not found"));

        return FacilityPackageMapper.toFacilityPackageDTO(facilityPackage);
    }

    public ListResponse<FacilityPackageDTO> getPackagesForManagement(
            UserDetailsImpl userDetails, Long facilityId, int page, int size) {

        if (userDetails == null) {
            throw new AccessDeniedException("로그인할 필요가 있습니다.");
        }

        boolean isAdmin = SecurityUtils.isAdmin(userDetails);

        if (!isAdmin) {
            Long ownerId = facilityRepository.findOwnerIdByFacilityId(facilityId)
                    .orElseThrow(() -> new ResourceNotFoundException("facility or owner not found"));

            if (!ownerId.equals(userDetails.getId())) {
                throw new AccessDeniedException("소유자가 아닙니다.");
            }
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("totalCount").descending());
        Page<FacilityPackage> projections = facilityPackageRepository.findByFacilityId(facilityId, pageable);

        Page<FacilityPackageDTO> packageDTOPage = projections.map(FacilityPackageMapper::toFacilityPackageDTO);

        return new ListResponse<>(
                packageDTOPage.getContent(),
                new PageInfo(page, size, projections.hasNext())
        );
    }

    public ListResponse<FacilityPackageDTO> getPublicPackages(Long facilityId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("totalCount").descending());

        Page<FacilityPackage> projections = facilityPackageRepository
                .findByFacilityIdAndActive(facilityId, true, pageable);

        Page<FacilityPackageDTO> packageDTOPage = projections.map(FacilityPackageMapper::toFacilityPackageDTO);

        List<FacilityPackageDTO> rs = packageDTOPage.getContent();

        return new ListResponse<>(rs, new PageInfo(page, size, projections.hasNext()));
    }

    public List<FacilityPackage> getAllPackages(List<Long> ids){
        return  facilityPackageRepository.findAllById(ids);
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
