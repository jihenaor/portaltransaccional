package com.serviciudad.model;

import com.serviciudad.models.factura.application.ports.FacturaResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FacturasResponse {
    private List<FacturaResponse> respuestafacturas;
}
