package com.catholic.ac.kr.booking_platform.facility.data;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityType;
import com.catholic.ac.kr.booking_platform.user.data.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Table(
        name = "facility",
        indexes = {
                @Index(columnList = "owner_id"),
                @Index(columnList = "name, active")
        }
)
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "facility_type")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public abstract class Facility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "facility_type", insertable = false, updatable = false)
    private FacilityType facilityType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User owner;

    @Column(nullable = false)
    private String address;

    private String instruction;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private boolean carPark;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean hasWifi;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isSuspended;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void create() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        isSuspended = false;
    }

    @PreUpdate
    protected void update() {
        updatedAt = LocalDateTime.now();
    }

    public void updateInfo(String name, String description, String instruction, String address) {
        if (name != null) this.name = name;
        if (description != null) this.description = description;
        if (instruction != null) this.instruction = instruction;
        if (address != null) this.address = address;
    }

    public void validateFacility() {
        if (this.isSuspended){
            throw new IllegalStateException("정지된 시설입니다");
        }
    }

    public void validateOperatingHours(LocalTime startTime) {
    }
}
