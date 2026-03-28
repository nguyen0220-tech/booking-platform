package booking_platform.repository;

import booking_platform.dto.RoleForBatchDTO;
import booking_platform.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("""
            SELECT new booking_platform.dto.RoleForBatchDTO(u.id,r.name)
            FROM User u
            JOIN  u.roles r
            WHERE u.id IN :userIds
            """)
    List<RoleForBatchDTO> findAllByUserIds(@Param("userIds") List<Long> userIds);
}