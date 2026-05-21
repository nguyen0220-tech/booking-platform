package com.catholic.ac.kr.booking_platform.facility.core.provider;

import com.catholic.ac.kr.booking_platform.facility.constant.RestaurantMenuAct;
import com.catholic.ac.kr.booking_platform.facility.core.provider.strategy_restaurant_menu.RestaurantMenuHandler;
import com.catholic.ac.kr.booking_platform.facility.data.resraurant.FacilityRestaurantRepository;
import com.catholic.ac.kr.booking_platform.facility.data.resraurant.Restaurant;
import com.catholic.ac.kr.booking_platform.facility.data.resraurant.RestaurantMenu;
import com.catholic.ac.kr.booking_platform.facility.data.resraurant.RestaurantMenuRepository;
import com.catholic.ac.kr.booking_platform.facility.dto.RestaurantMenuCommandRequest;
import com.catholic.ac.kr.booking_platform.facility.dto.RestaurantMenuRequest;
import com.catholic.ac.kr.booking_platform.facility.dto.RestaurantMenuUpdateRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.infrastructure.components.UploadHandler;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.ResourceNotFoundException;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.UnsupportedStrategyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RestaurantMenuCommandService {
    private final FacilityRestaurantRepository facilityRestaurantRepository;
    private final UploadHandler uploadHandler;
    private final RestaurantMenuRepository restaurantMenuRepository;
    private final Map<RestaurantMenuAct, RestaurantMenuHandler> restaurantMenuHandlers;

    public RestaurantMenuCommandService(
            FacilityRestaurantRepository facilityRestaurantRepository,
            UploadHandler uploadHandler,
            RestaurantMenuRepository restaurantMenuRepository,
            List<RestaurantMenuHandler> handlers) {
        this.facilityRestaurantRepository = facilityRestaurantRepository;
        this.uploadHandler = uploadHandler;
        this.restaurantMenuRepository = restaurantMenuRepository;
        this.restaurantMenuHandlers = handlers.stream().collect(Collectors.toMap(
                RestaurantMenuHandler::getRestaurantMenuAct,
                rm -> rm
        ));
    }

    public List<RestaurantMenu> getAllByRestaurantIds(List<Long> restaurantIds) {
        return restaurantIds != null ?
                restaurantMenuRepository.findAllByRestaurantIds(restaurantIds) : List.of();
    }

    public ApiResponse<String> addNewMenuForRestaurant(Long userId, RestaurantMenuRequest request) {
        Restaurant restaurant = facilityRestaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Facility not found"));

        validateOwner(userId, restaurant);
        validateRestaurantState(restaurant);

        RestaurantMenu newMenu = new RestaurantMenu();
        newMenu.setRestaurant(restaurant);
        newMenu.setName(request.getName());
        newMenu.setDescription(request.getDescription());
        newMenu.setPrice(request.getPrice());
        if (request.getFile() != null) {
            newMenu.setImageUrl(uploadMenuImage(userId, request.getFile()));
        }

        restaurantMenuRepository.save(newMenu);

        return ApiResponse.success(HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase(),
                "메뉴를 추가하셨습니다");
    }

    private String uploadMenuImage(Long userId, MultipartFile file) {
        return uploadHandler.uploadFile(userId, file);
    }

    @Transactional
    public ApiResponse<String> updateMenuForRestaurant(Long userId, RestaurantMenuUpdateRequest request) {
        RestaurantMenu menu = restaurantMenuRepository.findById(request.getMenuId())
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found"));

        Restaurant restaurant = menu.getRestaurant();

        validateOwner(userId, restaurant);
        validateRestaurantState(restaurant);

        if (isNotUpdated(menu, request)) {
            System.out.println("no changed");
            return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                    "메뉴 업데이트되었습니다");
        }

        menu.updateMenu(request.getName(), request.getDescription(), request.getPrice());

        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "메뉴 업데이트되었습니다");
    }

    private boolean isNotUpdated(RestaurantMenu menu, RestaurantMenuUpdateRequest updateRequest) {
        return Objects.equals(menu.getName(), updateRequest.getName()) &&
                Objects.equals(menu.getDescription(), updateRequest.getDescription()) &&
                menu.getPrice().compareTo(updateRequest.getPrice()) == 0;
    }

    private void validateOwner(Long currentUserId, Restaurant restaurant) {
        Long ownerId = restaurant.getOwner().getId();
        if (!ownerId.equals(currentUserId)) {
            throw new AccessDeniedException("소유자가 아닙니다");

        }
    }

    private void validateRestaurantState(Restaurant restaurant) {
        if (restaurant.isSuspended()) {
            throw new IllegalStateException("정지된 시설입니다");
        }
    }

    public ApiResponse<String> restaurantMenuCommand(Long userId, RestaurantMenuCommandRequest request) {
        RestaurantMenuHandler handler = restaurantMenuHandlers.get(request.getAct());

        if (handler == null) {
            throw new UnsupportedStrategyException(request.getAct().name());
        }

        return handler.restaurantMenuHandle(userId, request);
    }
}
