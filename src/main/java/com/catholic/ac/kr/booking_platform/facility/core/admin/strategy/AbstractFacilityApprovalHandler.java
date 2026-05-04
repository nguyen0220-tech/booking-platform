package com.catholic.ac.kr.booking_platform.facility.core.admin.strategy;

import com.catholic.ac.kr.booking_platform.facility.data.FacilityApproval;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityApprovalRequest;
import com.catholic.ac.kr.booking_platform.user.data.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractFacilityApprovalHandler implements FacilityApprovalHandle {

    public void setHandleRegistration(User admin, FacilityApproval registration, FacilityApprovalRequest request) {
        registration.setReviewer(admin);
        registration.setStatus(request.getStatus());

        String note = request.getNote();
        registration.setNote(note);
    }
}
