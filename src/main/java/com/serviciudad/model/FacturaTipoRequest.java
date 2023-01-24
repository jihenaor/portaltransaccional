package com.serviciudad.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class FacturaTipoRequest {
    @Schema(description = "Numero de la cuenta. Caracteres del 35 al 44 en el codigo de barras")
    @Size(min = 4, max = 20, message = "Error en datos")
    private String codsuscrip;
    @Schema(description = "Tipo de factura. Caracteres del 21 al 25 en el codigo de barras")
    @Size(min = 1, max = 2, message = "Error en datos")
    private String tipoFactura;

    @Schema(description = "Numero de la factura")
    @Size(max = 20, message = "Error en datos")
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
