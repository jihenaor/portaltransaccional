package com.serviciudad.compartido.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class ApiUnauthorized extends Exception {
    public ApiUnauthorized(String message) {
        super(message);
    }
}
