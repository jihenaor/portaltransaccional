package com.serviciudad.entity;

import com.serviciudad.compartido.model.ValueLongDomain;
import com.serviciudad.compartido.model.ValueObjectDomain;

public class IdRecaudoModel extends ValueObjectDomain {
    public IdRecaudoModel(String value) {
        super(value, 40);
    }
}
