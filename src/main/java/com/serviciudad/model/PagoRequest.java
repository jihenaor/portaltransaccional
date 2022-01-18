package com.serviciudad.model;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
public class PagoRequest {
    private String codsuscrip;
    private String idfactura;
}
