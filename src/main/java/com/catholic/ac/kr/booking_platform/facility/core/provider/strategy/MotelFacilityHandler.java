package com.catholic.ac.kr.booking_platform.facility.core.provider.strategy;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityType;
import com.catholic.ac.kr.booking_platform.facility.core.event.NewFacilityEvent;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityImageRepository;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityMotelRepository;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityRepository;
import com.catholic.ac.kr.booking_platform.facility.data.Motel;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityRequest;
import com.catholic.ac.kr.booking_platform.facility.dto.MotelDTO;
import com.catholic.ac.kr.booking_platform.facility.dto.MotelRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.user.data.User;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MotelFacilityHandler extends AbstractFacilityHandler<MotelDTO> {
    private final FacilityMotelRepository facilityMotelRepository;

    public MotelFacilityHandler(FacilityRepository f, FacilityImageRepository i, ApplicationEventPublisher e,
                                FacilityMotelRepository facilityMotelRepository) {
        super(f, i, e);
        this.facilityMotelRepository = facilityMotelRepository;
    }

    @Override
    public FacilityType getType() {
        return FacilityType.MOTEL;
    }

    @Override
    public List<MotelDTO> getSpecificDTOs(List<Long> ids) {
        return ids != null ? facilityMotelRepository.findMotelDTOsByIds(ids) : List.of();
    }

    @Override
    public ApiResponse<String> create(User owner, FacilityRequest baseRequest) {
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
}
