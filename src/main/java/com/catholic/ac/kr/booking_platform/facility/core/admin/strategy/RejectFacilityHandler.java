package com.catholic.ac.kr.booking_platform.facility.core.admin.strategy;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityStatus;
import com.catholic.ac.kr.booking_platform.facility.data.Facility;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityApproval;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityApprovalRepository;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityRepository;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityApprovalRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.user.data.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RejectFacilityHandler extends AbstractFacilityApprovalHandler {
    private final FacilityApprovalRepository facilityApprovalRepository;
    private final FacilityRepository facilityRepository;

    @Override
    public FacilityStatus getFacilityStatus() {
        return FacilityStatus.REJECTED;
    }

    @Override
    public ApiResponse<String> handleFacilityApproval(User admin, FacilityApproval approval, FacilityApprovalRequest request) {
        Facility facility = approval.getFacility();
        facility.setSuspended(true);

        setHandleRegistration(admin, approval, request);

        facilityApprovalRepository.save(approval);
        facilityRepository.save(facility);

        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "정상적으로 거절 처리되었습니다");
    }
}
