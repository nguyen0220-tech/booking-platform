package com.catholic.ac.kr.booking_platform.facility.core.admin.strategy;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityStatus;
import com.catholic.ac.kr.booking_platform.facility.core.event.FacilityApprovalEvent;
import com.catholic.ac.kr.booking_platform.facility.data.Facility;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityRegistration;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityRegistrationRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.user.data.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ApprovalFacilityHandler extends AbstractFacilityRegistrationHandler {
    private final ApplicationEventPublisher publisher;

    @Override
    public FacilityStatus getFacilityStatus() {
        return FacilityStatus.APPROVED;
    }

    @Transactional
    @Override
    public ApiResponse<String> handleFacilityRegistration(User admin, FacilityRegistration registration, FacilityRegistrationRequest request) {
        setHandleRegistration(admin, registration, request);
        Facility facility = registration.getFacility();
        facility.setActive(true);

        sendRegistrationApproval(registration);

        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "정상적으로 승인 처리되었습니다");
    }

    private void sendRegistrationApproval(FacilityRegistration registration) {
        String ownerName = registration.getFacility().getOwner().getFullName();
        String ownerEmail = registration.getFacility().getOwner().getEmail();
        String facilityName = registration.getFacility().getName();
        publisher.publishEvent(new FacilityApprovalEvent(ownerName, ownerEmail, facilityName));
    }
}
