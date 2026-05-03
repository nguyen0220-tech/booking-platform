package com.catholic.ac.kr.booking_platform.facility.data;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityStatus;
import com.catholic.ac.kr.booking_platform.user.data.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

//admin 관리
@Entity
@Table(
        name = "facility_approval",
        indexes = {@Index(columnList = "facility_id, status")}

)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FacilityApproval {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id", nullable = false)
    private Facility facility;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn( name = "admin_id")
    private User reviewer; //접수한 관리자

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FacilityStatus status;

    private String note; //거절된경우 사유

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime lastUpdatedAt;

    @PrePersist
    protected void create() {
        createdAt = LocalDateTime.now();
        lastUpdatedAt = LocalDateTime.now();
        reviewer = null;
        note = null;
    }

    @PreUpdate
    protected void update() {
        lastUpdatedAt = LocalDateTime.now();
    }
}
