package com.shopee.exceptions;

public class AppException extends RuntimeException {
    public AppException(String message) {
        super(message);
    }
}