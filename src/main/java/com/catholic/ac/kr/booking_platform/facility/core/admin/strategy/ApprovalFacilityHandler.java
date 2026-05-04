package com.catholic.ac.kr.booking_platform.facility.core.admin.strategy;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityStatus;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityApproval;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityApprovalRepository;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityApprovalRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.user.data.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApprovalFacilityHandler extends AbstractFacilityApprovalHandler {
    private final FacilityApprovalRepository facilityApprovalRepository;

    @Override
    public FacilityStatus getFacilityStatus() {
        return FacilityStatus.APPROVED;
    }

    @Override
    public ApiResponse<String> handleFacilityApproval(User admin,FacilityApproval approval, FacilityApprovalRequest request) {

        setHandleRegistration(admin, approval, request);

        facilityApprovalRepository.save(approval);

        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "정상적으로 승인 처리되었습니다");
    }
}
