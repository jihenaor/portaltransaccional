package com.serviciudad.entity;



import com.serviciudad.model.SessionRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

@Entity
@Table(name="auth")
@NoArgsConstructor
@Getter
@Setter
public final class AuthModel {
    @Id
    @Column(unique = true, nullable = false)
    private String id;

    private String cuenta;
    private String reference;
    private String descripcion;
    private long total;
    private String fecha;
    private String estado;
    private int requestid;
    private String autorizacion;
    private String fechaultimointento;
    private String pagoconfirmado;
    private String estadoevertec;

    public AuthModel(SessionRequest sessionRequest, int requestid, String id) {
        String pattern = "yyyy-MM-dd hh:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());

		this.id = id;
		this.cuenta = sessionRequest.getCuenta();
        this.reference = sessionRequest.getReference();
		this.descripcion = sessionRequest.getDescripcion();
		this.total = sessionRequest.getTotal();
        this.fecha = date;
        this.estado = "PENDING";
        this.requestid = requestid;
        this.pagoconfirmado = "N";
	}
}
