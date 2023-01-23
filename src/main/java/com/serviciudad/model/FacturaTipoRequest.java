package com.serviciudad.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FacturaTipoRequest {
    @Schema(description = "Numero de la cuenta. Caracteres del 35 al 44 en el codigo de barras")
    private String codsuscrip;
    @Schema(description = "Tipo de factura. Caracteres del 21 al 25 en el codigo de barras")
        private String tipoFactura;

    @Schema(description = "Numero de la factura")
    private String numerofactura;

    @Schema(description = "Valor de la factura")
    private Long valor;

    public FacturaTipoRequest(String codsuscrip, String tipoFactura) {
        this.codsuscrip = codsuscrip;
        this.tipoFactura = tipoFactura;
    }
    public FacturaTipoRequest(String codsuscrip, String tipoFactura, String numerofactura, Long valor) {
        this.codsuscrip = codsuscrip;
        this.tipoFactura = tipoFactura;
        this.numerofactura = numerofactura;
        this.valor = valor;
    }
}
