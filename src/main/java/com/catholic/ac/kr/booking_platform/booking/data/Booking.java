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
import org.springframework.security.access.AccessDeniedException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.EnumSet;
import java.util.Set;

@Table(
        name = "bookings",
        indexes = {
                @Index(columnList = "user_id"),
                @Index(columnList = "facility_owner_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "package_id", "usage_date", "start_time", "active_version"},
                        name = "unq_user_package_st")
        }
)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Booking {
    //취소 가능한 상태, 추후 상태 추가
    private static final Set<BookingStatus> CANCELLABLE_STATUSES = EnumSet.of(
            BookingStatus.PAID
    );

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
    private Long facilityOwnerId; //Chuẩn hóa ngược (Denormalization)

    @Column(nullable = false)
    private Long facilityId; //Denormalization

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
    private Long activeVersion;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void create() {
        this.createdAt = LocalDateTime.now();
        this.activeVersion = 0L;
    }

    public void validateUsageDate(LocalDate targetDate) {
        LocalDate today = LocalDate.now();
        if (targetDate.isBefore(today)) {
            throw new IllegalStateException("선택한 날짜가 지난 날짜입니다. 오늘 (" + today + ")");
        }
    }

    public void validateBookingTime(LocalDate targetUsageDate, LocalTime targetStartTime) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime requestDateTime = targetUsageDate.atTime(targetStartTime);

        if (requestDateTime.isBefore(now)) {
            throw new IllegalStateException("이미 지난 시간입니다. 마감 시간: " + targetStartTime);
        }
    }

    public void cancelBooking(Long userId) {
        //khi hủy sẽ hoàn tiền
        if (!this.user.getId().equals(userId)) {
            throw new AccessDeniedException("본 예약의 예약자가 아닙니다");
        }

        if (!CANCELLABLE_STATUSES.contains(this.status)) {
            throw new IllegalStateException("취소 불가능한 상태: " + this.status);
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();

        LocalDate cancelDeadline = this.usageDate.minusDays(1);
        LocalDate bookingDate = this.createdAt.toLocalDate();

        // 이용 1일전에 취소 가능
        boolean isBeforeDeadline = today.isBefore(cancelDeadline);

        // 당일 예약: 이용 시작시간 > now 해야함
        LocalDateTime startDateTime = this.usageDate.atTime(this.startTime);
        boolean isSameDayBooking = bookingDate.equals(this.usageDate)
                && today.equals(this.usageDate)
                && now.isBefore(startDateTime);

        if (!isBeforeDeadline && !isSameDayBooking) {
            throw new IllegalStateException("이용날짜 1일 전에 취소 가능 (이용날짜 " + this.usageDate + ")" +
                    "당일 예약은 시작시간 전에 취소 가능");
        }

        processCancel();
    }

    private void processCancel() {
        this.status = BookingStatus.CANCELLED;
        this.activeVersion = this.id;
    }
}
