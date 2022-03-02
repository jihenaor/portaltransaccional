package com.serviciudad.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FacturaRequest {
    private String codsuscrip;

    public FacturaRequest(String codsuscrip) {
        this.codsuscrip = codsuscrip;
    }
}
