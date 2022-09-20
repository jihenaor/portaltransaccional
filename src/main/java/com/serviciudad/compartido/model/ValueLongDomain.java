package com.serviciudad.compartido.model;

import com.serviciudad.compartido.exceptions.ExcepcionInformacionInvalida;
import com.serviciudad.compartido.propiedad.LimpiarXSS;
import lombok.AccessLevel;
import lombok.Getter;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class ValueLongDomain implements Serializable {

    private static final String REGEXP = "^\\d*$";

    @Getter(AccessLevel.NONE)
    private static final long serialVersionUID = 1L;

    @Getter
    protected final String value;

    public ValueLongDomain(String value) {
        this.value = validar(value);
    }

    public ValueLongDomain(String value, int longitudMaxima) {
        this.value = validar(value);
        if (value.length() > longitudMaxima) {
            throw new ExcepcionInformacionInvalida("Informacion invalida.");
        }
    }

    public ValueLongDomain() {
        this.value = null;
    }

    /**
     *
     * @param value String
     * @return String
     * @throws ExcepcionInformacionInvalida cadena con caracteres no permitidos
     */
    private String validar(String value) {
        String resultado;
        if(value!= null && !value.equals("")){
            resultado = validarCaracteresRestringidos(value);
        }else {
            resultado = value;
        }
        return resultado;
    }

    /**
     *
     * @param value String
     * @return String
     * @throws ExcepcionInformacionInvalida cadena con caracteres no permitidos
     */
    private String validarCaracteresRestringidos(String value) {
        String resultado;
        if (!Pattern.matches(REGEXP, value)) {
            throw new ExcepcionInformacionInvalida("Solo informacion numerica.");
        }
        resultado = LimpiarXSS.limpiar(value);
        return resultado;
    }

}
