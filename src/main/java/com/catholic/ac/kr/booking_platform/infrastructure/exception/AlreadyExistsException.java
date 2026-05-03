package com.catholic.ac.kr.booking_platform.infrastructure.exception;

public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String message) {
        super(message);
    }
}
