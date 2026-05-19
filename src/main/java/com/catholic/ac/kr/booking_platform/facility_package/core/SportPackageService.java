package com.catholic.ac.kr.booking_platform.facility_package.core;

import com.catholic.ac.kr.booking_platform.facility.data.FacilitySportRepository;
import com.catholic.ac.kr.booking_platform.facility.data.Sport;
import com.catholic.ac.kr.booking_platform.facility_package.data.SportPackage;
import com.catholic.ac.kr.booking_platform.facility_package.data.SportPackageRepository;
import com.catholic.ac.kr.booking_platform.facility_package.dto.SportPackageDTO;
import com.catholic.ac.kr.booking_platform.facility_package.dto.SportPackageRequest;
import com.catholic.ac.kr.booking_platform.facility_package.mapper.SportPackageMapper;
import com.catholic.ac.kr.booking_platform.facility_package.projection.SportPackageProjection;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.helper.response.ListResponse;
import com.catholic.ac.kr.booking_platform.helper.response.PageInfo;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.BadRequestException;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SportPackageService {
    private final SportPackageRepository sportPackageRepository;
    private final FacilitySportRepository facilitySportRepository;

    public ListResponse<SportPackageDTO> getPackagesByFacilityId(Long facilityId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.DESC, "startTime"));

        Page<SportPackageProjection> projections = sportPackageRepository.findBySportId(facilityId, pageable);

        Page<SportPackageDTO> dtoPage = projections.map(SportPackageMapper::toSportPackageDTO);

        List<SportPackageDTO> rs = dtoPage.getContent();

        return new ListResponse<>(rs, new PageInfo(page, size, projections.hasNext()));
    }

    public ApiResponse<String> createNewPackage(Long ownerId, SportPackageRequest request) {

        Sport sport = facilitySportRepository.findById(request.getFacilityId())
                .orElseThrow(() -> new ResourceNotFoundException("Facility not found"));

        if (!sport.getOwner().getId().equals(ownerId)) {
            throw new AccessDeniedException("소유자가 아닙니다");
        }

        checkValidationTime(sport, request.getStartTime(), request.getEndTime());

        SportPackage newPackage = new SportPackage();
        newPackage.setSport(sport);
        newPackage.setStartTime(request.getStartTime());
        newPackage.setEndTime(request.getEndTime());
        newPackage.setTotalPrice(
                calculatePrice(sport.getHourPrice(), request.getStartTime(), request.getEndTime())
        );

        sportPackageRepository.save(newPackage);

        return ApiResponse.success(HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase(),
                "새로 상품 패키지를 만드셨습니다");
    }

    private void checkValidationTime(Sport sport, LocalTime startTime, LocalTime endTime) {
        if (startTime == null || endTime == null || !startTime.isBefore(endTime)) {
            throw new BadRequestException("유효하지 않은 시간입니다 ");
        }

        boolean existPackage = sportPackageRepository.existsOverlappingSlot(sport.getId(), startTime, endTime);
        if (existPackage) {
            throw new BadRequestException("중복된 시간입니다");
        }
    }

    private BigDecimal calculatePrice(BigDecimal hourPrice, LocalTime startTime, LocalTime endTime) {
        Duration duration = Duration.between(startTime, endTime);

        BigDecimal totalHours = BigDecimal.valueOf(duration.toMinutes())
                .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);

        return hourPrice.multiply(totalHours);
    }
}
