package com.serviciudad.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PagoTipoFacturaRequest {
    @Schema(description = "Numero de la cuenta. Caracteres del 35 al 44 en el codigo de barras")
    private String codsuscrip;
    @Schema(description = "Numero de la factura. Caracteres del 26 al 34 en el codigo de barras")
    private String numerofactura;
    @Schema(description = "Tipo de factura. Caracteres del 21 al 25 en el codigo de barras")
    private String tipoFactura;
    @Schema(description = "Codigo del recaudador asignado por SERVICIUAD")
    private String banco;
    @Schema(description = "Valor de la factura")
    private long total;
    @Schema(description = "Fecha del recaudo en formato yyyy-MM-dd hh:mm:ss")
    private String fecha;
}
