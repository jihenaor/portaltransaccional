package com.serviciudad.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name="errores")
@NoArgsConstructor
@Getter
@Setter
public final class ErrorModel {
    @Id
    @Column(unique = true, nullable = false)
    private String id;
    private String msg;
    private String error;
    private String fecha;
    private String datos;
    private String origen;

    public ErrorModel(Exception e, String datos, String origen) {
        String errorTrace = "";
        String mensaje = "";
        if (e != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            errorTrace = sw.toString();

            mensaje = e.getMessage();
        }

        this.id = UUID.randomUUID().toString();
        this.msg = mensaje;
        this.error = errorTrace;
        this.fecha = (new Date()).toString();
        this.datos = datos;
        this.origen = origen;
    }
}