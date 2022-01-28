package com.serviciudad.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class SessionRequest {
	private final String cuenta;
    private final String reference;
    private final String descripcion;
    private final long total;
    private String autorizacion;
    private String login;
    private String trankey;
    private String nonce;
    private String seed;
}
