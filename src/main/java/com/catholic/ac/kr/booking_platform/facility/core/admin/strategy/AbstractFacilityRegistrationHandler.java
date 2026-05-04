package com.catholic.ac.kr.booking_platform.facility.core.admin.strategy;

import com.catholic.ac.kr.booking_platform.facility.data.FacilityRegistration;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityRegistrationRequest;
import com.catholic.ac.kr.booking_platform.user.data.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractFacilityRegistrationHandler implements FacilityRegistrationHandle {

    public void setHandleRegistration(User admin, FacilityRegistration registration, FacilityRegistrationRequest request) {
        registration.setReviewer(admin);
        registration.setStatus(request.getStatus());

        String note = request.getNote();
        registration.setNote(note);
    }
}
