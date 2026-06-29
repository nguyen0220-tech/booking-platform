//package com.catholic.ac.kr.booking_platform.helper;
//
//import com.catholic.ac.kr.booking_platform.booking.dto.BookingDTO;
//import com.catholic.ac.kr.booking_platform.facility.core.FacilityQueryService;
//import com.catholic.ac.kr.booking_platform.facility.data.Facility;
//import com.catholic.ac.kr.booking_platform.facility.dto.FacilityDTO;
//import com.catholic.ac.kr.booking_platform.facility.dto.FacilityMapper;
//import com.catholic.ac.kr.booking_platform.facility.dto.FacilityRegistrationDTO;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@RequiredArgsConstructor
//@Component
//public class BatchLoaderHandler {
//    private final FacilityQueryService facilityQueryService;
//
//    public  Map<BookingDTO, FacilityDTO> batchMappingForFacilities(List<Long> ids) {
//
//        List<Facility> facilities = facilityQueryService.getFacilityByIds(ids);
//
//        Map<Long, FacilityDTO> facilityOMap = facilities.stream()
//                .collect(Collectors.toMap(
//                        Facility::getId,
//                        FacilityMapper::toFacilityDTO
//                ));
//
//        return registrations.stream()
//                .collect(Collectors.toMap(
//                        r -> r,
//                        r -> facilityOMap.get(r.getFacilityId())
//                ));
//    }
//
//}
