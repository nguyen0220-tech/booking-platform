package com.catholic.ac.kr.booking_platform.facility_package.data;

import com.catholic.ac.kr.booking_platform.booking.data.Booking;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Table(name = "sport_package",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"id", "start_time", "end_time"})
        }
)
@DiscriminatorValue("SPORT")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SportPackage extends FacilityPackage {
    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Override
    public LocalTime getPackageStartTime() {
        return this.startTime;
    }

    @Override
    public LocalTime getPackageEndTime() {
        return this.endTime;
    }

    @Override
    public void applyTimeToBooking(Booking booking, LocalTime requestStartTim) {
        booking.setStartTime(this.getStartTime());
        booking.setEndTime(null);
    }
}
