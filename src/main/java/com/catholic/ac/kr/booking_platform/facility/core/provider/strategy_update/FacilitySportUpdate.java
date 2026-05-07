package com.catholic.ac.kr.booking_platform.facility.core.provider.strategy_update;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityType;
import com.catholic.ac.kr.booking_platform.facility.data.FacilitySportRepository;
import com.catholic.ac.kr.booking_platform.facility.data.Sport;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityInfoSportUpdateRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class FacilitySportUpdate extends AbstractFacilityUpdateHandler<Sport, FacilityInfoSportUpdateRequest>{
    private final FacilitySportRepository facilitySportRepository;

    public FacilitySportUpdate(FacilitySportRepository facilitySportRepository) {
        super();
        this.facilitySportRepository = facilitySportRepository;
    }

    @Override
    public FacilityType getFacilityType() {
        return FacilityType.SPORT;
    }

    @Override
    public ApiResponse<String> updateFacility(Sport sport, FacilityInfoSportUpdateRequest request) {
        updateFacilityInfo(sport, request);
        sport.updatePrice(request.getHourPrice());

        facilitySportRepository.save(sport);

        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "성공적으로 업데이트되었습니다");
    }
}
