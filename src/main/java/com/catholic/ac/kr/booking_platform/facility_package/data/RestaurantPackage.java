package com.catholic.ac.kr.booking_platform.facility_package.data;

import com.catholic.ac.kr.booking_platform.booking.data.Booking;
import com.catholic.ac.kr.booking_platform.facility.data.resraurant.RestaurantMenu;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.BadRequestException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Entity
@Table(
        name = "restaurant_package",
        indexes = @Index(columnList = "id")
)
@DiscriminatorValue("RESTAURANT")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RestaurantPackage extends FacilityPackage{
    @Column(nullable = false)
    private int maxCapacity;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "restaurant_package_menu", // table name
            joinColumns = @JoinColumn(name = "package_menu_id"),// FK
            inverseJoinColumns = @JoinColumn(name = "menu_id")// FK
    )
    private Set<RestaurantMenu> menus;

    @Override
    public void applyTimeToBooking(Booking booking, LocalDate requestUsageDate, LocalTime requestStartTime) {
        if (requestStartTime == null) {
            throw new BadRequestException("예약 시간이 없습니다");
        }
        booking.validateBookingTime(requestUsageDate, requestStartTime);
        booking.setStartTime(requestStartTime);
        booking.setEndTime(null);
    }
}
