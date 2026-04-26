package com.catholic.ac.kr.booking_platform.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Table(
        name = "users",
        indexes = {
                @Index(columnList = "email"),
                @Index(columnList = "username")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uni_username", columnNames = {"username"}),
                @UniqueConstraint(name = "uni_email", columnNames = {"email"}),
                @UniqueConstraint(name = "uni_phone", columnNames = {"phone"})
        }
)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_role", // table name
            joinColumns = @JoinColumn(name = "user_id"),// FK user
            inverseJoinColumns = @JoinColumn(name = "role_id")// FK role
    )
    private Set<Role> roles;

    private String avatarUrl;

    @Column(nullable = false)
    private boolean enabled;

    @Column(nullable = false)
    private boolean blocked;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void create() {
        avatarUrl = null;
    }

}
