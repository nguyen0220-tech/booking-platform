package com.catholic.ac.kr.booking_platform.facility.core;

import com.catholic.ac.kr.booking_platform.facility.data.*;
import com.catholic.ac.kr.booking_platform.facility.dto.*;
import com.catholic.ac.kr.booking_platform.infrastructure.components.UploadHandler;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.helper.response.ListResponse;
import com.catholic.ac.kr.booking_platform.helper.response.PageInfo;
import com.catholic.ac.kr.booking_platform.facility.constant.FacilityType;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.ResourceNotFoundException;
import com.catholic.ac.kr.booking_platform.facility.FacilityMapper;
import com.catholic.ac.kr.booking_platform.user.data.User;
import com.catholic.ac.kr.booking_platform.facility.api.FacilityProjection;
import com.catholic.ac.kr.booking_platform.facility.core.state.FacilityHandler;
import com.catholic.ac.kr.booking_platform.user.data.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
//@RequiredArgsConstructor
public class FacilityService {
    public final ApplicationEventPublisher eventPublisher;
    public final FacilityRepository facilityRepository;
    private final UserRepository userRepository;
    private final UploadHandler uploadHandler;
    private final FacilityImageRepository facilityImageRepository;
    private final FacilitySportRepository facilitySportRepository;
    private final FacilityMotelRepository facilityMotelRepository;
    private final FacilityRestaurantRepository facilityRestaurantRepository;
    private final FacilityApprovalRepository facilityApprovalRepository;

    private final Map<FacilityType, FacilityHandler> facilityHandlers;

// .   private final Map<FacilityType, BiFunction<User, FacilityRequest, ApiResponse<String>>> handlerMap = Map.of(
//            FacilityType.SPORT, this::createSportFacility,
//            FacilityType.MOTEL, this::createMotelFacility,
//            FacilityType.RESTAURANT, this::createRestaurantFacility
//    );

    public FacilityService(ApplicationEventPublisher eventPublisher, FacilityRepository facilityRepository,
                           UserRepository userRepository, UploadHandler uploadHandler,
                           FacilityImageRepository facilityImageRepository, FacilitySportRepository facilitySportRepository,
                           FacilityMotelRepository facilityMotelRepository, FacilityRestaurantRepository facilityRestaurantRepository,
                           FacilityApprovalRepository facilityApprovalRepository, List<FacilityHandler> facilityHandlers) {
        this.eventPublisher = eventPublisher;
        this.facilityRepository = facilityRepository;
        this.userRepository = userRepository;
        this.uploadHandler = uploadHandler;
        this.facilityImageRepository = facilityImageRepository;
        this.facilitySportRepository = facilitySportRepository;
        this.facilityMotelRepository = facilityMotelRepository;
        this.facilityRestaurantRepository = facilityRestaurantRepository;
        this.facilityApprovalRepository = facilityApprovalRepository;
        this.facilityHandlers = facilityHandlers.stream()
                .collect(Collectors.toMap(FacilityHandler::getType, h -> h));
    }

    public FacilityDTO getFacilityById(Long ownerId, Long id) {
        FacilityProjection projection = facilityRepository.findFacilityById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Facility not found"));

        if (!ownerId.equals(projection.getOwnerId())) {
            throw new AccessDeniedException("access denied");
        }
        return FacilityMapper.toFacilityDTO(projection);
    }

    @PreAuthorize("hasRole('PROVIDER')")
    public ListResponse<FacilityDTO> getFacilitiesByOwnerId(Long ownerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<FacilityProjection> facilityProjections = facilityRepository.findByOwnerId(ownerId, pageable);

        Page<FacilityDTO> facilityDTOS = facilityProjections.map(FacilityMapper::toFacilityDTO);

        List<FacilityDTO> rs = facilityDTOS.getContent();

        return new ListResponse<>(rs, new PageInfo(page, size, facilityProjections.hasNext()));
    }

    public List<Facility> getFacilityByIds(List<Long> facilityIds) {
        return facilityRepository.findAllById(facilityIds);
    }

    public List<FacilityImageDTO> getFacilityImageByEntityIds(List<Long> entityIds) {
        return facilityImageRepository.findAllByEntityIdIdIn(entityIds);
    }

    public List<SportDTO> getFacilitySportByIds(List<Long> ids) {
        return ids != null ? facilitySportRepository.findSportDTOsByIds(ids) : List.of();
    }

    public List<MotelDTO> getFacilityMotelByIds(List<Long> ids) {
        return ids != null ? facilityMotelRepository.findMotelDTOsByIds(ids) : List.of();
    }

    public List<RestaurantDTO> getFacilityRestaurantByIds(List<Long> ids) {
        return ids != null ? facilityRestaurantRepository.findRestaurantDTOsByIds(ids) : List.of();
    }

    public List<FacilityApproval> getFacilityApprovalByIds(List<Long> ids) {
        return facilityApprovalRepository.findAllById(ids);
    }

    @PreAuthorize("hasRole('PROVIDER')")
    public ListResponse<FacilityDTO> searchFacilityByKeyword(Long ownerId, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<FacilityProjection> projection = facilityRepository.findByOwnerIdAndKeyword(ownerId, keyword, pageable);

        List<FacilityDTO> rs = projection.stream()
                .map(FacilityMapper::toFacilityDTO)
                .toList();

        return new ListResponse<>(rs, new PageInfo(page, size, projection.hasNext()));
    }

    @PreAuthorize("hasRole('PROVIDER')")
    public ApiResponse<String> createFacility(Long ownerId, FacilityRequest request) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));

        FacilityHandler handler = facilityHandlers.get(request.getType());
        return handler.create(owner, request);
//        return handlerMap.getOrDefault(request.getType(), this::unsupported).apply(owner, request);
    }

    /*.

    private ApiResponse<String> createSportFacility(User owner, FacilityRequest baseRequest) {
        SportRequest request = (SportRequest) baseRequest;
        Sport newSport = new Sport();

        setBasicFacility(owner, newSport, request);
        newSport.setHourPrice(request.getHourPrice());

        facilityRepository.save(newSport);
        saveFacilityImages(newSport.getId(), request.getType(), request.getImages());
        eventPublisher.publishEvent(new NewFacilityEvent(newSport));

        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                request.getName() + "등록 접수가 되었습니다");

    }

    private ApiResponse<String> createMotelFacility(User owner, FacilityRequest baseRequest) {
        MotelRequest request = (MotelRequest) baseRequest;
        Motel newMotel = new Motel();

        setBasicFacility(owner, newMotel, request);
        newMotel.setHourPrice(request.getHourPrice());
        newMotel.setNightPrice(request.getNightPrice());

        facilityRepository.save(newMotel);
        saveFacilityImages(newMotel.getId(), request.getType(), request.getImages());
        eventPublisher.publishEvent(new NewFacilityEvent(newMotel));

        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                request.getName() + "등록 접수가 되었습니다");

    }

    private ApiResponse<String> createRestaurantFacility(User owner, FacilityRequest baseRequest) {
        RestaurantRequest request = (RestaurantRequest) baseRequest;
        Restaurant newRestaurant = new Restaurant();

        setBasicFacility(owner, newRestaurant, request);
        newRestaurant.setFoodType(request.getFoodType());

        facilityRepository.save(newRestaurant);
        saveFacilityImages(newRestaurant.getId(), request.getType(), request.getImages());
        eventPublisher.publishEvent(new NewFacilityEvent(newRestaurant));


        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                request.getName() + "등록 접수가 되었습니다");

    }

    private void setBasicFacility(User owner, Facility facility, FacilityRequest request) {
        facility.setOwner(owner);
        facility.setName(request.getName());
        facility.setDescription(request.getDescription());
        facility.setAddress(request.getAddress());
        facility.setInstruction(request.getInstruction());
        facility.setActive(request.isActive());
        facility.setCarPark(request.isCarPark());
        facility.setHasWifi(request.isHasWifi());
    }

    private ApiResponse<String> unsupported(User owner, FacilityRequest request) {
        throw new BadRequestException("지원하지 않는 타입입니다: " + request.getType());
    }

    private void saveFacilityImages(Long entityId, FacilityType type, List<String> images) {
        if (images == null || images.isEmpty()) {
            return;
        }

        List<FacilityImage> list = new ArrayList<>();
        for (String image : images) {
            FacilityImage facilityImage = new FacilityImage();
            facilityImage.setEntityId(entityId);
            facilityImage.setType(type);
            facilityImage.setImageUrl(image);

            list.add(facilityImage);
        }
        facilityImageRepository.saveAll(list);
    }
     */

    public ApiResponse<List<String>> uploadFacilityImage(Long ownerId, List<MultipartFile> images) {
        List<String> rs = new ArrayList<>();

        for (MultipartFile file : images) {
            String imageUrl = uploadHandler.uploadFile(ownerId, file);
            rs.add(imageUrl);
        }
        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "success", rs);
    }
}
