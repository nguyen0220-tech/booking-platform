package com.catholic.ac.kr.booking_platform.booking.web;

import com.catholic.ac.kr.booking_platform.booking.core.BookingQueryService;
import com.catholic.ac.kr.booking_platform.booking.data.Booking;
import com.catholic.ac.kr.booking_platform.booking.dto.BookingDTO;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityMapper;
import com.catholic.ac.kr.booking_platform.facility.core.FacilityQueryService;
import com.catholic.ac.kr.booking_platform.facility.data.Facility;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityDTO;
import com.catholic.ac.kr.booking_platform.facility_package.core.FacilityPackageService;
import com.catholic.ac.kr.booking_platform.facility_package.data.FacilityPackage;
import com.catholic.ac.kr.booking_platform.facility_package.dto.FacilityPackageDTO;
import com.catholic.ac.kr.booking_platform.facility_package.dto.FacilityPackageMapper;
import com.catholic.ac.kr.booking_platform.helper.response.ListResponse;
import com.catholic.ac.kr.booking_platform.infrastructure.security.userdetails.SecurityUtils;
import com.catholic.ac.kr.booking_platform.infrastructure.security.userdetails.UserDetailsImpl;
import com.catholic.ac.kr.booking_platform.user.constant.RoleName;
import com.catholic.ac.kr.booking_platform.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class BookingResolver {
    private final BookingQueryService bookingQueryService;
    private final FacilityPackageService facilityPackageService;
    private final FacilityQueryService facilityQueryService;

    @QueryMapping
    public ListResponse<BookingDTO> bookings(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Argument RoleName roleName,
            @Argument int page,
            @Argument int size) {
        return bookingQueryService.getBookingsWithRole(userDetails.getId(), roleName, page, size);
    }

    @QueryMapping
    public ListResponse<BookingDTO> upcomingBookings(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Argument long daysLatter) {
        return bookingQueryService.getUpcomingBookings(userDetails.getId(), daysLatter);
    }

    @QueryMapping
    public BookingDTO booking(@AuthenticationPrincipal UserDetailsImpl userDetails, @Argument Long bookingId) {
        return bookingQueryService.getBookingById(userDetails.getId(), bookingId);
    }

    @SchemaMapping(typeName = "Booking")
    public UserDTO user(@AuthenticationPrincipal UserDetailsImpl userDetails, BookingDTO booking) {

        if (!hasPermission(userDetails, booking)) {
            return null;
        }

        return new UserDTO(booking.getUserId());
    }

    private boolean hasPermission(UserDetailsImpl userDetails, BookingDTO booking) {
        Long currentUserId = userDetails.getId();
        boolean myBooking = currentUserId.equals(booking.getUserId());
        boolean isAdmin = SecurityUtils.isAdmin(userDetails);
        boolean facilityOwner = currentUserId.equals(booking.getFacilityOwnerId());

        return myBooking || isAdmin || facilityOwner;
    }


    @BatchMapping(typeName = "Booking")
    public Map<BookingDTO, FacilityPackageDTO> packageInfo(Principal principal, List<BookingDTO> bookings) {
        UserDetailsImpl userDetails = SecurityUtils.getUserDetails(principal);

        if (userDetails == null) {
            return null;
        }

        List<Long> packageIdsAuthorized = bookings.stream()
                .filter(b -> hasPermission(userDetails, b))
                .map(BookingDTO::getFacilityPackageId)
                .toList();

        Map<Long, FacilityPackageDTO> facilityPackageMap = new HashMap<>();

        if (!packageIdsAuthorized.isEmpty()) {
            List<FacilityPackage> packages = facilityPackageService.getAllPackages(packageIdsAuthorized);
            facilityPackageMap = packages.stream()
                    .collect(Collectors.toMap(
                            FacilityPackage::getId,
                            FacilityPackageMapper::toFacilityPackageDTO
                    ));

        }

        Map<BookingDTO, FacilityPackageDTO> result = new HashMap<>();
        for (BookingDTO booking : bookings) {
            if (hasPermission(userDetails, booking)) {
                result.put(booking, facilityPackageMap.get(booking.getFacilityPackageId()));
            } else
                result.put(booking, null);
        }

        return result;
    }

    @BatchMapping(typeName = "Booking")
    public Map<BookingDTO, FacilityDTO> facility(List<BookingDTO> bookings) {
        List<Long> facilityIds = bookings.stream()
                .map(BookingDTO::getFacilityId)
                .toList();

        List<Facility> facilities = facilityQueryService.getFacilityByIds(facilityIds);

        Map<Long, FacilityDTO> map = facilities.stream()
                .collect(Collectors.toMap(
                        Facility::getId,
                        FacilityMapper::toFacilityDTO
                ));

        return bookings.stream()
                .collect(Collectors.toMap(
                        b -> b,
                        b -> map.get(b.getFacilityId())
                ));
    }

    @BatchMapping(typeName = "FacilityPackage", field = "selectedDate")
    public Map<FacilityPackageDTO, List<LocalDate>> selectedDate(List<FacilityPackageDTO> facilityPackages) {
        List<Long> packageIds = facilityPackages.stream()
                .map(FacilityPackageDTO::getId)
                .toList();

        List<Booking> bookings = bookingQueryService.getAllByPackageIds(packageIds);

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
