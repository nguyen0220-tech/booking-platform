package com.catholic.ac.kr.booking_platform.repository;

import com.catholic.ac.kr.booking_platform.enumdef.TokenType;
import com.catholic.ac.kr.booking_platform.model.TokenVerify;
import com.catholic.ac.kr.booking_platform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface TokenVerifyRepository extends JpaRepository<TokenVerify, Long> {
    TokenVerify findByTokenAndType(String token, TokenType type);

    @Modifying
    @Query("""
            DELETE FROM TokenVerify t WHERE t.id = :id
            """)
    void deleteByIdCustom(@Param("id") Long id);

    Optional<TokenVerify> findByUserAndType(User user, TokenType type);
}
