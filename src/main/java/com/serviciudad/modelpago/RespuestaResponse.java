package com.serviciudad.modelpago;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class RespuestaResponse {
    private final String date;
    private final String currency;
    private final String total;
    private final String reference;
    private final String status;
    private final String message;
    private final String authorization;
    private final String cuenta;
    private final String pagoregistrado;
}
