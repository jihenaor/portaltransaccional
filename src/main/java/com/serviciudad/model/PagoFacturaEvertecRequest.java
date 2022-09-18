package com.serviciudad.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PagoFacturaEvertecRequest {
    private final String codsuscrip;
    private final String numerofactura;
    private final String banco;
    private final String requestid;
    private final long total;
    private final String fecha;
}
