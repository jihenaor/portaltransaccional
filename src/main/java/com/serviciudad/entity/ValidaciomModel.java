package com.serviciudad.entity;


import com.serviciudad.model.SessionRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@AllArgsConstructor
@Getter
public final class ValidaciomModel {
    private final String cuenta;
    private final String reference;
    private final long total;
    private final String fecha;
    private final String pagoconfirmado;
}
