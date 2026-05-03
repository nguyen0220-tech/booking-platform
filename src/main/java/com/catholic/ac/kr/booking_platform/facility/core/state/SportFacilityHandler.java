package com.catholic.ac.kr.booking_platform.facility.core.state;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityType;
import com.catholic.ac.kr.booking_platform.facility.core.event.NewFacilityEvent;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityImageRepository;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityRepository;
import com.catholic.ac.kr.booking_platform.facility.data.FacilitySportRepository;
import com.catholic.ac.kr.booking_platform.facility.data.Sport;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityRequest;
import com.catholic.ac.kr.booking_platform.facility.dto.SportDTO;
import com.catholic.ac.kr.booking_platform.facility.dto.SportRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.user.data.User;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component

public class SportFacilityHandler extends AbstractFacilityHandler<SportDTO> {

    private final FacilitySportRepository facilitySportRepository;

    public SportFacilityHandler(FacilityRepository f, FacilityImageRepository i, ApplicationEventPublisher e, FacilitySportRepository facilitySportRepository) {
        super(f, i, e);
        this.facilitySportRepository = facilitySportRepository;
    }

    @Override
    public FacilityType getType() {
        return FacilityType.SPORT;
    }

    @Override
    public List<SportDTO> getSpecificDTOs(List<Long> ids) {
        return ids != null ? facilitySportRepository.findSportDTOsByIds(ids) : List.of();
    }

    @Override
    public ApiResponse<String> create(User owner, FacilityRequest baseRequest) {
        SportRequest request = (SportRequest) baseRequest;
        Sport newSport = new Sport();
        setBasicFacility(owner, newSport, request);
        newSport.setHourPrice(request.getHourPrice());

        facilityRepository.save(newSport);
        saveFacilityImages(newSport.getId(), getType(), request.getImages());
        eventPublisher.publishEvent(new NewFacilityEvent(newSport));

        return ApiResponse.success(200, "OK", request.getName() + " 등록 접수가 되었습니다");
    }
}
