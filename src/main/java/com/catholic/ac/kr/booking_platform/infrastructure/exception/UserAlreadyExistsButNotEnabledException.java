package com.catholic.ac.kr.booking_platform.infrastructure.exception;

public class UserAlreadyExistsButNotEnabledException extends RuntimeException {
    public UserAlreadyExistsButNotEnabledException(String message) {
        super(message);
    }
}
