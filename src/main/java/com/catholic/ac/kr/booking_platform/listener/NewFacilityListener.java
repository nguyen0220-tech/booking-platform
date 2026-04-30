package com.catholic.ac.kr.booking_platform.listener;

import com.catholic.ac.kr.booking_platform.enumdef.EquipmentStatus;
import com.catholic.ac.kr.booking_platform.event.NewFacilityEvent;
import com.catholic.ac.kr.booking_platform.model.FacilityApproval;
import com.catholic.ac.kr.booking_platform.repository.FacilityApprovalRepository;
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

        newFac.setStatus(EquipmentStatus.PENDING);
        newFac.setFacility(event.facility());

        facilityApprovalRepository.save(newFac);
    }
}
