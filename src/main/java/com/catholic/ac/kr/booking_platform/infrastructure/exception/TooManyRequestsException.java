package com.catholic.ac.kr.booking_platform.infrastructure.exception;

public class TooManyRequestsException extends RuntimeException {
    public TooManyRequestsException(String message) {
        super(message);
    }
}
