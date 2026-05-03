package com.catholic.ac.kr.booking_platform.infrastructure.exception;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String message) {
        super(message);
    }
}
