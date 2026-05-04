package com.catholic.ac.kr.booking_platform.facility.core.listener;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityStatus;
import com.catholic.ac.kr.booking_platform.facility.core.event.NewFacilityEvent;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityRegistration;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityRegistrationRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class NewFacilityListener {

    private final FacilityRegistrationRepository facilityRegistrationRepository;

    public NewFacilityListener(FacilityRegistrationRepository facilityRegistrationRepository) {
        this.facilityRegistrationRepository = facilityRegistrationRepository;
    }

    @EventListener
    public void newEquipmentHandle(NewFacilityEvent event) {
        FacilityRegistration newFac = new FacilityRegistration();

        newFac.setStatus(FacilityStatus.PENDING);
        newFac.setFacility(event.facility());

        facilityRegistrationRepository.save(newFac);
    }
}
