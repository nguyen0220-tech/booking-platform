package com.catholic.ac.kr.booking_platform.facility_package.data;

import com.catholic.ac.kr.booking_platform.booking.data.Booking;
import com.catholic.ac.kr.booking_platform.facility.constant.FacilityType;
import com.catholic.ac.kr.booking_platform.facility.data.Facility;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.BadRequestException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Table(
        name = "facility_package",
        indexes = {@Index(columnList = "facility_id, active")}
)
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "facility_type")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public abstract class FacilityPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String packageName;

    private String note;

    @Column(nullable = false)
    private int totalCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id", nullable = false)
    private Facility facility;

    @Enumerated(EnumType.STRING)
    @Column(name = "facility_type", insertable = false, updatable = false)
    private FacilityType facilityType;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private BigDecimal salePrice;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void create() {
        this.createdAt = LocalDateTime.now();
        this.totalCount = 0;
        this.active = true;
    }

    public abstract void applyTimeToBooking(Booking booking, LocalTime requestStartTime);

    public void validatePackage(){
        if (!this.isActive()){
            throw new BadRequestException("비활성한 패키지입니다");
        }
    }
}
