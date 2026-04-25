package com.catholic.ac.kr.booking_platform.service;

import com.catholic.ac.kr.booking_platform.enumdef.TokenType;
import com.catholic.ac.kr.booking_platform.exception.BadRequestException;
import com.catholic.ac.kr.booking_platform.model.TokenVerify;
import com.catholic.ac.kr.booking_platform.model.User;
import com.catholic.ac.kr.booking_platform.repository.TokenVerifyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenVerifyService {
    private final TokenVerifyRepository tokenVerifyRepository;

    public TokenVerifyService(TokenVerifyRepository tokenVerifyRepository) {
        this.tokenVerifyRepository = tokenVerifyRepository;
    }

    public String createToken(User user, TokenType type) {
        Optional<TokenVerify> existingToken = tokenVerifyRepository.findByUserAndType(user, type);

        TokenVerify tokenVerify;
        if (existingToken.isPresent()) {
            tokenVerify = existingToken.get();
            if (tokenVerify.getCreated().isAfter(LocalDateTime.now().minusMinutes(1))) {
                throw new BadRequestException("1분 후에 시도해주세요.");

            }
            tokenVerify.setToken(UUID.randomUUID().toString());
            tokenVerify.setCreated(LocalDateTime.now());
            tokenVerify.setExpiryDate(LocalDateTime.now().plusMinutes(1));
        } else {
            String token = UUID.randomUUID().toString();
            tokenVerify = new TokenVerify();
            tokenVerify.setUser(user);
            tokenVerify.setType(type);
            tokenVerify.setToken(token);
            tokenVerify.setCreated(LocalDateTime.now());
            tokenVerify.setExpiryDate(LocalDateTime.now().plusMinutes(1));
            tokenVerify.setType(type);
        }

        tokenVerifyRepository.save(tokenVerify);

        return tokenVerify.getToken();
    }
}
