package com.serviciudad.model;

import com.serviciudad.modelpago.PaymentPago;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClientRequest {
    private String locale;
    private Auth auth;
    private Payment payment;
    private String expiration;
    private String returnUrl;
    private String ipAddress;
    private String userAgent;

}
