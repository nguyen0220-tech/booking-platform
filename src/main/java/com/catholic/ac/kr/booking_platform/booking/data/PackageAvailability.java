package com.catholic.ac.kr.booking_platform.booking.data;

import com.catholic.ac.kr.booking_platform.facility_package.data.FacilityPackage;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "package_availability",
        indexes = {@Index(columnList = "package_id, target_date")}
)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PackageAvailability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", nullable = false)
    private FacilityPackage facilityPackage;

    private LocalDate targetDate;

    private int bookedCount;

    @Version
    private Long version;
}
