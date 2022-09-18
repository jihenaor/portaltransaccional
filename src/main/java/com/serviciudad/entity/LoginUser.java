package com.serviciudad.entity;

import com.serviciudad.compartido.model.ValueObjectDomain;

public class LoginUser extends ValueObjectDomain {
    public LoginUser(String value) {
        super(value, 20);
    }
}
