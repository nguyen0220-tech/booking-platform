package com.catholic.ac.kr.booking_platform.repository;

import com.catholic.ac.kr.booking_platform.dto.RoleForBatchDTO;
import com.catholic.ac.kr.booking_platform.enumdef.RoleName;
import com.catholic.ac.kr.booking_platform.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("""
            SELECT new com.catholic.ac.kr.booking_platform.dto.RoleForBatchDTO(u.id,r.name)
            FROM User u
            JOIN  u.roles r
            WHERE u.id IN :userIds
            """)
    List<RoleForBatchDTO> findAllByUserIds(@Param("userIds") List<Long> userIds);

    Optional<Role> findByName(RoleName roleName);
}