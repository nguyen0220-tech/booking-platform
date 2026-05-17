//package com.catholic.ac.kr.booking_platform.facility_package.data;
//
//import com.catholic.ac.kr.booking_platform.facility.data.Facility;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.time.LocalDateTime;
//
//@Table(
//        indexes = {
//                @Index(columnList = "facility_id", name = "inx_facility_id")
//        }
//)
//@Entity
//@AllArgsConstructor
//@NoArgsConstructor
//@Getter
//@Setter
//public class FacilityPackage {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "facility_id", nullable = false)
//    private Facility facility;
//
//    @Column(nullable = false)
//    private LocalDateTime startTime;
//
//    @Column(nullable = false)
//    private LocalDateTime endTime;
//
//    @Column(nullable = false)
//    private LocalDateTime createdAt;
//
//    @PrePersist
//    protected void create() {
//        createdAt = LocalDateTime.now();
//    }
//}
