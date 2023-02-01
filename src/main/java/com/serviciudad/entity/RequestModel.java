package com.serviciudad.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name="request")
@NoArgsConstructor
@Getter
@Setter
public final class RequestModel {
    @Id
    @Column(unique = true, nullable = false)
    private String id;
    private String codsuscrip;
    private String numerofactura;
    private String tipoFactura;
    private String banco;
    private String codigoRespuesta;
    private String comentario;
    private String fecha;
    private long total;

    public RequestModel(String codsuscrip,
                        String numerofactura,
                        String tipoFactura,
                        String codigoRespuesta,
                        String comentario) {
        this.id = UUID.randomUUID().toString();
        this.fecha = (new Date()).toString();
        this.codsuscrip = codsuscrip;
        this.numerofactura = numerofactura;
        this.tipoFactura = tipoFactura;
        this.codigoRespuesta = codigoRespuesta;
        this.comentario = comentario;

    }
}
