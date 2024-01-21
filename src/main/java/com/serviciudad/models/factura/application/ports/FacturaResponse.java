package com.serviciudad.models.factura.application.ports;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)

public class FacturaResponse {
    @Schema(description = "Codigo de respuesta <ul><li>0 Datos de factura no on validos</ul><li>1 registo exitoso</li></ul>")
    private Integer codRespuesta;
    private String descripcion;
    private String idfactura;
    private String cuenta;
    @Schema(description = "Tipo de factura", example = "00 - Pago total")
    private Integer tipofact;
    @Schema(description = "Valor del la última factura")
    private Long totalfactura;
    @Schema(description = "Fecha maxima de pago")
    private String fechapago;
    private String status;
    @Schema(description = "Estado del pago", example = "S pago aplicado, N pago no aplicado")
    private String aplicado;
    private String fechaultimopago;
    private String facturavencida;
    private String ciclo;
}
