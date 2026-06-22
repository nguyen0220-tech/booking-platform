//package com.catholic.ac.kr.booking_platform.analytics.data;
//
//import com.catholic.ac.kr.booking_platform.user.constant.RoleName;
//import com.catholic.ac.kr.booking_platform.user.data.User;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.time.LocalDateTime;
//import java.time.YearMonth;
//
//@Table(
//        indexes = @Index(columnList = "user_id")
//)
//@Entity
//@Inheritance(strategy = InheritanceType.JOINED)
//@DiscriminatorColumn(name = "role")
//@AllArgsConstructor
//@NoArgsConstructor
//@Getter
//@Setter
//public class Analytics {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//
//    @Column(nullable = false, insertable = false, updatable = false)
//    @Enumerated(EnumType.STRING)
//    private RoleName role;
//
//    @Column(nullable = false)
//    private Integer totalBookings;
//
//    @Column(nullable = false)
//    private Integer totalCancelledBookings;
//
//    @Column(nullable = false)
//    private Integer monthlyBookings;
//
//    @Column(nullable = false)
//    private Integer totalRating;
//
//    @Column(nullable = false)
//    private YearMonth yearMonth;
//
//    @Column(nullable = false)
//    private LocalDateTime updateTime;
//
//    @PrePersist
//    protected void create() {
//        this.updateTime = LocalDateTime.now();
//    }
//
//    @PreUpdate
//    protected void update() {
//        this.yearMonth = YearMonth.now();
//        this.updateTime = LocalDateTime.now();
//    }
//}
