package com.catholic.ac.kr.booking_platform.service;

import com.catholic.ac.kr.booking_platform.model.TokenVerify;
import com.catholic.ac.kr.booking_platform.model.User;
import com.catholic.ac.kr.booking_platform.repository.TokenVerifyRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TokenVerifyService {
    private final TokenVerifyRepository tokenVerifyRepository;

    public TokenVerifyService(TokenVerifyRepository tokenVerifyRepository) {
        this.tokenVerifyRepository = tokenVerifyRepository;
    }

    public String createToken(User user) {
        TokenVerify tokenVerify = new TokenVerify();

        String token = UUID.randomUUID().toString();

        tokenVerify.setToken(token);
        tokenVerify.setUser(user);

        tokenVerifyRepository.save(tokenVerify);

        return token;
    }
}
