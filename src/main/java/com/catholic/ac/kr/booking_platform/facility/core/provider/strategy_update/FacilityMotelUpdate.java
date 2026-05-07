package com.catholic.ac.kr.booking_platform.facility.core.provider.strategy_update;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityType;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityMotelRepository;
import com.catholic.ac.kr.booking_platform.facility.data.Motel;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityInfoMotelUpdateRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class FacilityMotelUpdate extends AbstractFacilityUpdateHandler<Motel, FacilityInfoMotelUpdateRequest> {
    private final FacilityMotelRepository facilityMotelRepository;

    public FacilityMotelUpdate(FacilityMotelRepository facilityMotelRepository) {
        super();
        this.facilityMotelRepository = facilityMotelRepository;
    }

    @Override
    public FacilityType getFacilityType() {
        return FacilityType.MOTEL;
    }

    @Override
    public ApiResponse<String> updateFacility(Motel motel, FacilityInfoMotelUpdateRequest request) {

        updateFacilityInfo(motel, request);
        motel.updateMotelPrices(request.getHourPrice(),request.getNightPrice());

        facilityMotelRepository.save(motel);

        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "성공적으로 업데이트되었습니다");
    }
}
