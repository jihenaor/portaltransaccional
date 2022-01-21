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
    private final String cus;
    private final String status;
    private final String message;
}
