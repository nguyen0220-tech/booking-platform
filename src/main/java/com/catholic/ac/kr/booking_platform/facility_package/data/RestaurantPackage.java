package com.catholic.ac.kr.booking_platform.facility_package.data;

import com.catholic.ac.kr.booking_platform.booking.data.Booking;
import com.catholic.ac.kr.booking_platform.facility.data.resraurant.RestaurantMenu;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.BadRequestException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    public LocalTime getPackageStartTime() {
        return null;
    }

    @Override
    public LocalTime getPackageEndTime() {
        return null;
    }

    @Override
    public void applyTimeToBooking(Booking booking, LocalTime requestStartTime) {
        if (requestStartTime == null) {
            throw new BadRequestException("예약 시간이 없습니다");
        }
        booking.setStartTime(requestStartTime);
    }
}
