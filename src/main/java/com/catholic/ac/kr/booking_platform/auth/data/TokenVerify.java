package com.catholic.ac.kr.booking_platform.auth.data;

import com.catholic.ac.kr.booking_platform.auth.core.TokenType;
import com.catholic.ac.kr.booking_platform.user.data.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Table(
        name = "token_verify",
        indexes = {
                @Index(columnList = "token,type"),
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uni_userId_type", columnNames = {"user_id","type"})
        }
)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TokenVerify {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TokenType type;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @Column(nullable = false)
    private LocalDateTime created;
}
