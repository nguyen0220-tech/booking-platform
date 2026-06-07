package com.catholic.ac.kr.booking_platform.booking.web;

import com.catholic.ac.kr.booking_platform.booking.core.BookingService;
import com.catholic.ac.kr.booking_platform.booking.data.Booking;
import com.catholic.ac.kr.booking_platform.facility_package.dto.FacilityPackageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class BookingResolver {
    private final BookingService bookingService;

    @BatchMapping(typeName = "FacilityPackage", field = "selectedDate")
    public Map<FacilityPackageDTO, List<LocalDate>> selectedDate(List<FacilityPackageDTO> facilityPackages) {
        List<Long> packageIds = facilityPackages.stream()
                .map(FacilityPackageDTO::getId)
                .toList();

        System.out.println("packageIds " + packageIds);
        List<Booking> bookings = bookingService.getAllByPackageIds(packageIds);

        Map<Long, List<LocalDate>> map = bookings.stream()
                .collect(Collectors.groupingBy(
                        b -> b.getFacilityPackage().getId(),
                        Collectors.mapping(Booking::getUsageDate, Collectors.toList())
                ));

        return facilityPackages.stream()
                .collect(Collectors.toMap(
                        p -> p,
                        p -> map.getOrDefault(p.getId(), List.of())
                ));
    }
}
