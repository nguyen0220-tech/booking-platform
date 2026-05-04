package com.catholic.ac.kr.booking_platform.facility.core.admin.strategy;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityStatus;

import com.catholic.ac.kr.booking_platform.facility.data.FacilityApproval;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityApprovalRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.BadRequestException;
import com.catholic.ac.kr.booking_platform.user.data.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PendingFacilityHandler implements FacilityApprovalHandle{
    @Override
    public FacilityStatus getFacilityStatus() {
        return FacilityStatus.PENDING;
    }

    @Override
    public ApiResponse<String> handleFacilityApproval(User admin, FacilityApproval approval, FacilityApprovalRequest request) {
        throw new BadRequestException("비정상적인 처리입니다");
    }
}
