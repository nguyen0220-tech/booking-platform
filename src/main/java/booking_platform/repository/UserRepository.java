package booking_platform.repository;

import booking_platform.model.User;
import booking_platform.projection.UserProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("""
            SELECT u.id AS id
            FROM User u
            """)
    Page<UserProjection> findAllUser(Pageable pageable);

    Optional<User> findByUsername(String username);
}
