package com.serviciudad.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
public final class HelloResponse {
    private String hello;

    public HelloResponse() {
        this.hello = "Ok " +  new Date();
    }
}
