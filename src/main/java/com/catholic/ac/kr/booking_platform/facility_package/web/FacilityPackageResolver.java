package com.catholic.ac.kr.booking_platform.facility_package.web;

import com.catholic.ac.kr.booking_platform.facility.dto.FacilityDTO;
import com.catholic.ac.kr.booking_platform.facility_package.core.FacilityPackageService;
import com.catholic.ac.kr.booking_platform.facility_package.dto.*;
import com.catholic.ac.kr.booking_platform.helper.response.ListResponse;
import com.catholic.ac.kr.booking_platform.infrastructure.config.VirtualThreadExecutor;
import com.catholic.ac.kr.booking_platform.infrastructure.security.userdetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class FacilityPackageResolver {
    private final VirtualThreadExecutor executor;
    private final FacilityPackageService facilityPackageService;

    @QueryMapping
    public ListResponse<FacilityPackageDTO> facilityPackages(
            @Argument Long facilityId,
            @Argument int page,
            @Argument int size,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return facilityPackageService.getPackagesForManagement(userDetails, facilityId, page, size);
    }

    @BatchMapping(typeName = "FacilityPackage", field = "packageTarget")
    public Mono<Map<FacilityPackageDTO, Object>> packageTarget(List<FacilityPackageDTO> facilityPackages) {
        Map<String, List<Long>> idsGroupByType = getUdsGroupByType(facilityPackages);

        CompletableFuture<Map<FacilityPackageDTO, Object>> future = CompletableFuture.supplyAsync(
                () -> executeParallelFetch(facilityPackages, idsGroupByType), executor.executorService());

        return Mono.fromFuture(future);
    }

    private Map<String, List<Long>> getUdsGroupByType(List<FacilityPackageDTO> packages) {
        return packages.stream()
                .collect(Collectors.groupingBy(
                        FacilityPackageDTO::getFacilityType,
                        Collectors.mapping(FacilityPackageDTO::getId, Collectors.toList())
                ));
    }

    @SuppressWarnings("DuplicatedCode")
    private Map<FacilityPackageDTO, Object> executeParallelFetch(
            List<FacilityPackageDTO> packages,
            Map<String, List<Long>> idsGroupByType) {

        var sportTask = CompletableFuture.supplyAsync(
                () -> facilityPackageService.getSportPackages(idsGroupByType.get("SPORT")), executor.executorService());
        var motelTask = CompletableFuture.supplyAsync(
                () -> facilityPackageService.getMotelPackages(idsGroupByType.get("MOTEL")), executor.executorService());
        var restTask = CompletableFuture.supplyAsync(
                () -> facilityPackageService.getRestaurantPackages(idsGroupByType.get("RESTAURANT")), executor.executorService());

        Map<Long, SportPackageDTO> spMap = sportTask.join().stream()
                .collect(Collectors.toMap(
                        SportPackageDTO::getId,
                        sportPackageDTO -> sportPackageDTO
                ));

        Map<Long, MotelPackageDTO> mpMap = motelTask.join().stream()
                .collect(Collectors.toMap(
                        MotelPackageDTO::getId,
                        motelPackageDTO -> motelPackageDTO
                ));

        Map<Long, RestaurantPackageDTO> rpMap = restTask.join().stream()
                .collect(Collectors.toMap(
                        RestaurantPackageDTO::getId,
                        restaurantPackageDTO -> restaurantPackageDTO
                ));

        Map<FacilityPackageDTO, Object> result = new HashMap<>();

        for (FacilityPackageDTO facilityPackage : packages) {
            Object value = switch (facilityPackage.getFacilityType()) {
                case "SPORT" -> spMap.get(facilityPackage.getId());
                case "MOTEL" -> mpMap.get(facilityPackage.getId());
                case "RESTAURANT" -> rpMap.get(facilityPackage.getId());
                default -> null;
            };
            result.put(facilityPackage, value);
        }
        return result;
    }

    @BatchMapping(typeName = "RestaurantPackage", field = "menus")
    public Map<RestaurantPackageDTO, List<RestaurantPackageMenuDTO>> menus(List<RestaurantPackageDTO> restaurantPackages) {
        List<Long> packageIds = restaurantPackages.stream()
                .map(RestaurantPackageDTO::getId)
                .toList();

        List<RestaurantPackageMenuDTO> allMenus = facilityPackageService.getRestaurantPackageMenus(packageIds);

        Map<Long, List<RestaurantPackageMenuDTO>> map = allMenus.stream()
                .collect(Collectors.groupingBy(
                        RestaurantPackageMenuDTO::getPackageId,
                        Collectors.mapping(p -> p, Collectors.toList())
                ));

        return restaurantPackages.stream()
                .collect(Collectors.toMap(
                        rp -> rp,
                        rp -> map.get(rp.getId())
                ));
    }

    @SchemaMapping(typeName = "Facility", field = "packages")
    public ListResponse<FacilityPackageDTO> packages(
            FacilityDTO facility,
            @Argument int page,
            @Argument int size) {

        return facilityPackageService.getPublicPackages(facility.getId(), page, size);
    }
}
