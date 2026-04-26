package com.catholic.ac.kr.booking_platform.exception;

public class UserAlreadyExistsButNotEnabledException extends RuntimeException {
    public UserAlreadyExistsButNotEnabledException(String message) {
        super(message);
    }
}
