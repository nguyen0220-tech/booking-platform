package com.catholic.ac.kr.booking_platform.facility.core.listener;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityStatus;
import com.catholic.ac.kr.booking_platform.facility.core.event.NewFacilityEvent;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityApproval;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityApprovalRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class NewFacilityListener {

    private final FacilityApprovalRepository facilityApprovalRepository;

    public NewFacilityListener(FacilityApprovalRepository facilityApprovalRepository) {
        this.facilityApprovalRepository = facilityApprovalRepository;
    }

    @EventListener
    public void newEquipmentHandle(NewFacilityEvent event) {
        FacilityApproval newFac = new FacilityApproval();

        newFac.setStatus(FacilityStatus.PENDING);
        newFac.setFacility(event.facility());

        facilityApprovalRepository.save(newFac);
    }
}
