package com.serviciudad.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PagoFacturaResponse {
    private String codigoRespuesta;
    private String comentario;
    private Long numerofactura;
}
