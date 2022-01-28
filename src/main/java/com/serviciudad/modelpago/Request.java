package com.serviciudad.modelpago;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Request{
    private String locale;
    private Payer payer;
    public PaymentPago payment;
    public List<Field> fields;
    public String returnUrl;
    public String ipAddress;
    public String userAgent;
    private String expiration;
}
