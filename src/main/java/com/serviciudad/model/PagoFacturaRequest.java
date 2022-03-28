package com.serviciudad.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PagoFacturaRequest {
    private final String codsuscrip;
    private final String numerofactura;
    private final String banco;
    private final String requestid;
    private final long total;
    private final String fecha;
}
