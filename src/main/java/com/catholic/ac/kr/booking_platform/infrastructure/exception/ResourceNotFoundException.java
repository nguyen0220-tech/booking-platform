package com.catholic.ac.kr.booking_platform.infrastructure.exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
