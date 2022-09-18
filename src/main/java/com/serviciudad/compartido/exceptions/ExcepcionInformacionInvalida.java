package com.serviciudad.compartido.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
@Getter
public final class ExcepcionInformacionInvalida extends DomainError {
    private static final long serialVersionUID = 1L;

    private final String errorMessage;

    public ExcepcionInformacionInvalida(String errorMessage) {
        super(errorMessage);

        this.errorMessage = errorMessage;
    }

}
