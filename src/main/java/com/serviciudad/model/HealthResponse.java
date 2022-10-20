package com.serviciudad.model;

import lombok.Getter;

import java.util.Date;

@Getter
public final class HealthResponse {
    private String apipublica;

    private String fecha;

    public HealthResponse() {
        this.apipublica = "Ok";
        this.fecha = "" +  new Date();
    }
}
