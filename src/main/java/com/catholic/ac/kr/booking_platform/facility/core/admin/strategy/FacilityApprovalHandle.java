package com.catholic.ac.kr.booking_platform.facility.core.admin.strategy;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityStatus;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityApproval;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityApprovalRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.user.data.User;

public interface FacilityApprovalHandle {
    FacilityStatus getFacilityStatus();

    ApiResponse<String> handleFacilityApproval(User admin, FacilityApproval approval, FacilityApprovalRequest request);
}
