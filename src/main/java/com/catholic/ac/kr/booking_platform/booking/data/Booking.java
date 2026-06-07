package com.catholic.ac.kr.booking_platform.booking.data;

import com.catholic.ac.kr.booking_platform.booking.constant.BookingStatus;
import com.catholic.ac.kr.booking_platform.booking.constant.PayMethod;
import com.catholic.ac.kr.booking_platform.facility_package.data.FacilityPackage;
import com.catholic.ac.kr.booking_platform.user.data.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Table(
        name = "bookings",
        indexes = {
                @Index(columnList = "user_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "package_id", "usage_date", "start_time"},
                        name = "unq_user_package_st")
        }
)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", nullable = false)
    private FacilityPackage facilityPackage;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private BigDecimal basisPrice;

    @Column(nullable = false)
    private LocalDate usageDate;

    @Column(nullable = false)
    private LocalTime startTime;

    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PayMethod payMethod;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void create() {
        createdAt = LocalDateTime.now();
    }

    public void validateUsageDate(LocalDate targetDate){
        LocalDate today = LocalDate.now();
        if (targetDate.isBefore(today)) {
            throw new IllegalStateException("선택한 날짜가 지난 날짜입니다. 오늘 (" + today + ")");
        }
    }

    public void validateBookingTime(LocalDate targetUsageDate, LocalTime targetStartTime) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime requestDateTime = targetUsageDate.atTime(targetStartTime);

        if (requestDateTime.isBefore(now)) {
            throw new IllegalStateException("이미 지난 시간입니다. 마감 시간: "+targetStartTime);
        }
    }
}
