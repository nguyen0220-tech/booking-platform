package com.catholic.ac.kr.booking_platform.facility.web;

import com.catholic.ac.kr.booking_platform.facility.dto.FacilityMapper;
import com.catholic.ac.kr.booking_platform.facility.core.FacilityImageService;
import com.catholic.ac.kr.booking_platform.facility.core.FacilityQueryService;
import com.catholic.ac.kr.booking_platform.facility.core.provider.RestaurantMenuCommandService;
import com.catholic.ac.kr.booking_platform.facility.data.Facility;
import com.catholic.ac.kr.booking_platform.facility.data.resraurant.RestaurantMenu;
import com.catholic.ac.kr.booking_platform.facility.dto.*;
import com.catholic.ac.kr.booking_platform.helper.response.ListResponse;
import com.catholic.ac.kr.booking_platform.infrastructure.config.VirtualThreadExecutor;
import com.catholic.ac.kr.booking_platform.infrastructure.security.userdetails.SecurityUtils;
import com.catholic.ac.kr.booking_platform.infrastructure.security.userdetails.UserDetailsImpl;
import com.catholic.ac.kr.booking_platform.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class FacilityResolver {
    private final VirtualThreadExecutor executor;
    private final FacilityImageService facilityImageService;
    private final FacilityQueryService facilityQueryService;
    private final RestaurantMenuCommandService restaurantMenuCommandService;

    @QueryMapping
    public FacilityDTO facility(@Argument Long id) {
        return facilityQueryService.getFacilityById(id);
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
    ) {
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

    @SuppressWarnings("DuplicatedCode")
    @BatchMapping(typeName = "Facility", field = "facilityTarget")
    public Mono<Map<FacilityDTO, Object>> facilityTarget(List<FacilityDTO> facilities) {
        Map<String, List<Long>> idsGroupByType = facilities.stream()
                .collect(Collectors.groupingBy(
                        FacilityDTO::getFacilityType,
                        Collectors.mapping(FacilityDTO::getId, Collectors.toList())
                ));

        List<Long> sportFacilityIds = idsGroupByType.getOrDefault("SPORT", List.of());
        List<Long> motelFacilityIds = idsGroupByType.getOrDefault("MOTEL", List.of());
        List<Long> restaurantFacilityIds = idsGroupByType.getOrDefault("RESTAURANT", List.of());

        CompletableFuture<Map<FacilityDTO, Object>> futureResult = CompletableFuture.supplyAsync(() -> {

            CompletableFuture<List<SportDTO>> sportTask = CompletableFuture.supplyAsync(() -> {
                log("sportTask");
                return facilityQueryService.getFacilitySportByIds(sportFacilityIds);
            }, executor.executorService());

            CompletableFuture<List<MotelDTO>> motelTask = CompletableFuture.supplyAsync(() -> {
                log("motelTask");
                return facilityQueryService.getFacilityMotelByIds(motelFacilityIds);
            }, executor.executorService());

            CompletableFuture<List<RestaurantDTO>> restaurantTask = CompletableFuture.supplyAsync(() -> {
                log("restaurantTask");
                return facilityQueryService.getFacilityRestaurantByIds(restaurantFacilityIds);
            }, executor.executorService());

            // Đợi cả 3 xong (Vì đang ở trong Virtual Thread nên .join() không sợ nghẽn)
            Map<Long, SportDTO> sportMap = sportTask.join().stream()
                    .collect(Collectors.toMap(SportDTO::getId, s -> s));
            Map<Long, MotelDTO> motelMap = motelTask.join().stream()
                    .collect(Collectors.toMap(MotelDTO::getId, m -> m));
            Map<Long, RestaurantDTO> restaurantMap = restaurantTask.join().stream()
                    .collect(Collectors.toMap(RestaurantDTO::getId, r -> r));

            Map<FacilityDTO, Object> result = new HashMap<>();
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
        }, executor.executorService());

        return Mono.fromFuture(futureResult);
    }

    public static void log(String message) {
        System.out.println(LocalTime.now() + " | [" + Thread.currentThread().getName() + "] | " + message);
    }

    @BatchMapping(typeName = "Restaurant", field = "menus")
    public Map<RestaurantDTO, List<RestaurantMenuDTO>> menus(List<RestaurantDTO> restaurants) {
        List<Long> restaurantIds = restaurants.stream()
                .map(RestaurantDTO::getId)
                .distinct()
                .toList();

        List<RestaurantMenu> allMenu = restaurantMenuCommandService.getAllByRestaurantIds(restaurantIds);

        Map<Long, List<RestaurantMenuDTO>> map = allMenu.stream()
                .collect(Collectors.groupingBy(
                        m -> m.getRestaurant().getId(),
                        Collectors.mapping(FacilityMapper::toRestaurantMenuDTO, Collectors.toList())
                ));

        return restaurants.stream()
                .collect(Collectors.toMap(
                        r -> r,
                        m -> map.getOrDefault(m.getId(), List.of())
                ));
    }

    @SchemaMapping(typeName = "Facility", field = "owner")
    public UserDTO owner(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            FacilityDTO facility) {

        if (userDetails == null) {
            return null;
        }

        boolean isAdmin = SecurityUtils.isAdmin(userDetails);
        Long currentId = userDetails.getId();
        Long ownerId = facility.getOwnerId();

        if (!isAdmin && !currentId.equals(ownerId)) {
            return null;
        }

        return new UserDTO(ownerId);
    }

}
