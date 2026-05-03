package com.catholic.ac.kr.booking_platform.facility.data;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Table(
        name = "facility_image",
        indexes = {@Index(columnList = "entity_id")}
)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class FacilityImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long entityId;

    @Column(nullable = false)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FacilityType type;

    private LocalDateTime createdAt;

    @PrePersist
    protected void create() {
        createdAt = LocalDateTime.now();
    }
}
