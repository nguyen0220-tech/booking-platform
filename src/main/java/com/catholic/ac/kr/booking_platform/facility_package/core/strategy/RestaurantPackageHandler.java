package com.catholic.ac.kr.booking_platform.facility_package.core.strategy;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityType;
import com.catholic.ac.kr.booking_platform.facility.data.resraurant.FacilityRestaurantRepository;
import com.catholic.ac.kr.booking_platform.facility.data.resraurant.Restaurant;
import com.catholic.ac.kr.booking_platform.facility.data.resraurant.RestaurantMenu;
import com.catholic.ac.kr.booking_platform.facility.data.resraurant.RestaurantMenuRepository;
import com.catholic.ac.kr.booking_platform.facility_package.data.FacilityPackageRepository;
import com.catholic.ac.kr.booking_platform.facility_package.data.RestaurantPackage;
import com.catholic.ac.kr.booking_platform.facility_package.dto.RestaurantPackageRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.BadRequestException;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Set;

@Component
public class RestaurantPackageHandler extends AbstractPackageHandler<RestaurantPackageRequest> {
    private final FacilityRestaurantRepository facilityRestaurantRepository;
    private final RestaurantMenuRepository restaurantMenuRepository;

    public RestaurantPackageHandler(FacilityPackageRepository packageRepository, FacilityRestaurantRepository facilityRestaurantRepository, RestaurantMenuRepository restaurantMenuRepository) {
        super(packageRepository);
        this.facilityRestaurantRepository = facilityRestaurantRepository;
        this.restaurantMenuRepository = restaurantMenuRepository;
    }

    @Override
    public FacilityType getFacilityType() {
        return FacilityType.RESTAURANT;
    }

    @Override
    public ApiResponse<String> processCreate(Long ownerId, Long restaurantId, RestaurantPackageRequest request) {
        Restaurant restaurant = facilityRestaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        validateFacility(ownerId, restaurant);

        Set<RestaurantMenu> menus = getMenus(restaurantId, request.getMenuIds());

        BigDecimal basicPrice = restaurantPriceCalculator(menus);
        if (basicPrice.compareTo(request.getSalePrice()) < 0) {
            throw new BadRequestException("할인 가겨이 원가격(" + basicPrice + ")보다 큽니다");
        }

        RestaurantPackage restaurantPackage = new RestaurantPackage();

        setBasicPackage(restaurantPackage, request);

        restaurantPackage.setFacility(restaurant);
        restaurantPackage.setMenus(menus);
        restaurantPackage.setMaxCapacity(request.getCapacity());
        restaurantPackage.setPrice(basicPrice);
        restaurantPackage.setSalePrice(request.getSalePrice());

        packageRepository.save(restaurantPackage);

        return buildResponseSuccess(request.getPackageName());
    }

    private BigDecimal restaurantPriceCalculator(Set<RestaurantMenu> menus) {
        return menus.stream()
                .map(RestaurantMenu::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Set<RestaurantMenu> getMenus(Long restaurantId, Set<Long> menuIds) {
        Set<RestaurantMenu> menus = restaurantMenuRepository.findAllByMenuIds(restaurantId, menuIds);

        // Nếu lấy lên không đủ số lượng ID truyền vào -> Có ID không hợp lệ
        if (menus.size() != menuIds.size()) {
            throw new BadRequestException("유효하지 않은 메뉴가 포함되어 있습니다.");
        }
        return menus;
    }
}
