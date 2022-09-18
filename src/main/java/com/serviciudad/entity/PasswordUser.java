package com.serviciudad.entity;

import com.serviciudad.compartido.model.ValueObjectDomain;

public class PasswordUser extends ValueObjectDomain {
    public PasswordUser(String value) {
        super(value, 20);
    }
}
