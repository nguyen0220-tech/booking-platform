package com.catholic.ac.kr.booking_platform.facility.web;

import com.catholic.ac.kr.booking_platform.facility.core.FacilityApprovalService;
import com.catholic.ac.kr.booking_platform.facility.core.FacilityImageService;
import com.catholic.ac.kr.booking_platform.facility.core.FacilityQueryService;
import com.catholic.ac.kr.booking_platform.facility.dto.*;
import com.catholic.ac.kr.booking_platform.helper.response.ListResponse;
import com.catholic.ac.kr.booking_platform.facility.FacilityMapper;
import com.catholic.ac.kr.booking_platform.user.UserMapper;
import com.catholic.ac.kr.booking_platform.facility.data.Facility;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityApproval;
import com.catholic.ac.kr.booking_platform.user.data.User;
import com.catholic.ac.kr.booking_platform.infrastructure.security.userdetails.UserDetailsImpl;
import com.catholic.ac.kr.booking_platform.user.core.UserManageService;
import com.catholic.ac.kr.booking_platform.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class FacilityResolver {
    private final UserManageService userManageService;
    private final FacilityImageService facilityImageService;
    private final FacilityQueryService facilityQueryService;
    private final FacilityApprovalService facilityApprovalService;

    @QueryMapping
    public FacilityDTO facility(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Argument Long id){
        return facilityQueryService.getFacilityById(userDetails.getId(),id);
    }

    @QueryMapping
    public ListResponse<FacilityDTO> facilities(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Argument int page,
            @Argument int size) {
        return facilityQueryService.getFacilitiesByOwnerId(userDetails.getId(), page, size);
    }

    @QueryMapping
    public ListResponse<FacilityDTO> facilitiesByKeyword(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Argument String keyword,
            @Argument int page,
            @Argument int size
    ){
        return facilityQueryService.searchFacilityByKeyword(userDetails.getId(), keyword, page, size);
    }

    @BatchMapping(typeName = "Facility", field = "facilityInfo")
    public Map<FacilityDTO, FacilityInfoDTO> facilityInfo(List<FacilityDTO> facilities) {
        List<Long> facilityIds = facilities.stream()
                .map(FacilityDTO::getId)
                .toList();

        List<Facility> facilityList = facilityQueryService.getFacilityByIds(facilityIds);

        Map<Long, FacilityInfoDTO> map = facilityList.stream()
                .collect(Collectors.toMap(
                        Facility::getId,
                        FacilityMapper::convertToFacilityInFfo
                ));

        return facilities.stream()
                .collect(Collectors.toMap(
                        f -> f,
                        f -> map.get(f.getId())
                ));
    }

    @BatchMapping(typeName = "Facility", field = "imageUrls")
    public Map<FacilityDTO, List<String>> imageUrls(List<FacilityDTO> facilities) {
        List<Long> facilityIds = facilities.stream()
                .map(FacilityDTO::getId)
                .toList();

        List<FacilityImageDTO> facilityList = facilityImageService.getFacilityImageByEntityIds(facilityIds);

        Map<Long, List<String>> map = facilityList.stream()
                .collect(Collectors.groupingBy(
                        FacilityImageDTO::getEntityId,
                        Collectors.mapping(FacilityMapper::convertToFacilityImageUrl, Collectors.toList())
                ));

        return facilities.stream()
                .collect(Collectors.toMap(
                        f -> f,
                        f -> map.getOrDefault(f.getId(), List.of())
                ));
    }

    @BatchMapping(typeName = "Facility", field = "facilityTarget")
    public Map<FacilityDTO, Object> facilityTarget(List<FacilityDTO> facilities) {
        Map<String, List<Long>> idsGroupByType = facilities.stream()
                .collect(Collectors.groupingBy(
                        FacilityDTO::getFacilityType,
                        Collectors.mapping(FacilityDTO::getId, Collectors.toList())
                ));

        List<Long> sportFacilityIds = idsGroupByType.getOrDefault("SPORT", List.of());
        List<Long> motelFacilityIds = idsGroupByType.getOrDefault("MOTEL", List.of());
        List<Long> restaurantFacilityIds = idsGroupByType.getOrDefault("RESTAURANT", List.of());

        List<SportDTO> sports = facilityQueryService.getFacilitySportByIds(sportFacilityIds);
        List<MotelDTO> motels = facilityQueryService.getFacilityMotelByIds(motelFacilityIds);
        List<RestaurantDTO> restaurants = facilityQueryService.getFacilityRestaurantByIds(restaurantFacilityIds);

        Map<Long, SportDTO> sportMap = sports.stream()
                .collect(Collectors.toMap(
                        SportDTO::getId,
                        s -> s
                ));

        Map<Long, MotelDTO> motelMap = motels.stream()
                .collect(Collectors.toMap(
                        MotelDTO::getId,
                        m -> m
                ));

        Map<Long, RestaurantDTO> restaurantMap = restaurants.stream()
                .collect(Collectors.toMap(
                        RestaurantDTO::getId,
                        r -> r
                ));

        Map<FacilityDTO, Object> result = new ConcurrentHashMap<>();

        for (FacilityDTO facility : facilities) {
            Object value = switch (facility.getFacilityType()) {
                case "SPORT" -> sportMap.get(facility.getId());
                case "MOTEL" -> motelMap.get(facility.getId());
                case "RESTAURANT" -> restaurantMap.get(facility.getId());
                default -> null;
            };
            result.put(facility, value);
        }

        return result;
    }

    @BatchMapping(typeName = "Facility", field = "owner")
    public Map<FacilityDTO, UserDTO> owner(List<FacilityDTO> facilities) {
        List<Long> ownerIds = facilities.stream()
                .map(FacilityDTO::getOwnerId)
                .distinct()
                .toList();

        System.out.println("userId: " + ownerIds);

        List<User> ownerList = userManageService.getAllByIds(ownerIds);

        Map<Long, UserDTO> map = ownerList.stream()
                .collect(Collectors.toMap(
                        User::getId,
                        UserMapper::convertToUserDTO
                ));

        return facilities.stream()
                .collect(Collectors.toMap(
                        f -> f,
                        f -> map.get(f.getOwnerId())
                ));
    }

    @BatchMapping(typeName = "Facility", field = "approvalStatus")
    public Map<FacilityDTO, FacilityApprovalDTO> approvalStatus(List<FacilityDTO> facilities) {
        List<Long> facilityIds = facilities.stream()
                .map(FacilityDTO::getId)
                .toList();

        List<FacilityApproval> facilityApprovalList = facilityApprovalService.getFacilityApprovalByIds(facilityIds);

        Map<Long,FacilityApprovalDTO> map = facilityApprovalList.stream()
                .collect(Collectors.toMap(
                        FacilityApproval::getId,
                        FacilityMapper::convertToFacilityApprovalDTO
                ));

        return facilities.stream()
                .collect(Collectors.toMap(
                        f -> f,
                        f -> map.get(f.getId())
                ));
    }
}
