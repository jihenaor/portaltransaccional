package com.serviciudad.modelpago;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PagoResponse {
    private int requestId;
    private Status status;
    private Request request;
    private List<Payment> payment;
    private Object subscription;
}
