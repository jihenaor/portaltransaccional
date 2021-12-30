package com.serviciudad.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FacturaResponse {
    private Integer CodRespuesta;
    private String descripcion;
    private String idfactura;
    private String cuenta;
    private Integer tipofact;
    private Long totalfactura;
    private String fechapago;

    
    
}
