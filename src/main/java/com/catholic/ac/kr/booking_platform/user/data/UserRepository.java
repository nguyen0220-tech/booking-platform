package com.catholic.ac.kr.booking_platform.user.data;

import com.catholic.ac.kr.booking_platform.user.constant.RoleName;
import com.catholic.ac.kr.booking_platform.user.api.UserProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("""
            SELECT u.id AS id
            FROM User u
            """)
    Page<UserProjection> findAllUser(Pageable pageable);

    @Query("""
            SELECT u.id AS id
            FROM User u WHERE u.username = :username
            """)
    UserProjection findUserByUsername(@Param("username") String username);

    @Query("""
            SELECT u.id AS id
            FROM User u WHERE u.email = :email
            """)
    UserProjection findUserByEmail(@Param("email") String email);

    @Query("""
            SELECT u.id AS id
            FROM User u WHERE u.enabled = :is
            """)
    Page<UserProjection> filterUserEnabled(Pageable pageable, @Param("is") boolean is);

    @Query("""
            SELECT u.id AS id
            FROM User u WHERE u.blocked = :is
            """)
    Page<UserProjection> filterUserBlocked(Pageable pageable, @Param("is") boolean is);

    @Query("""
            SELECT u.id AS id
            FROM User u
            JOIN u.roles r
            WHERE r.name = :name
            """)
    Page<UserProjection> findByRoleName(@Param("name") RoleName name, Pageable pageable);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    Optional<User> findByEmail(String email);

    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.enabled = false AND u.createdAt < :expiryTime")
    int deleteUnenabledUsersOlderThan(@Param("expiryTime") LocalDateTime expiryTime);
}
