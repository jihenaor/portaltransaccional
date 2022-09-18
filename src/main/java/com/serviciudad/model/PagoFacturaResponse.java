package com.serviciudad.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PagoFacturaResponse {
    @Schema(description = "0 Datos de factura no on validos,  1 registo exitoso")
    private String codigoRespuesta;
    @Schema(description = "descripcion del error")
    private String comentario;
    @Schema(description = "numero de la factura que se ha intentado registrar")
    private Long numerofactura;
}
