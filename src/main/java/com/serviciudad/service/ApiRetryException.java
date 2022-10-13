package com.serviciudad.service;

public class ApiRetryException extends RuntimeException {
    public ApiRetryException(String message) {
        super(message);
    }
}