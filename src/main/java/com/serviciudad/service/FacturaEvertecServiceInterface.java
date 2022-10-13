package com.serviciudad.service;

import com.serviciudad.exception.DomainExceptionCuentaNoExiste;
import com.serviciudad.model.FacturaRequest;
import com.serviciudad.model.FacturaResponse;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public interface FacturaEvertecServiceInterface {
    @Retryable(value = DomainExceptionCuentaNoExiste.class)
    FacturaResponse consultarFactura(FacturaRequest facturaRequest) throws DomainExceptionCuentaNoExiste;

    FacturaResponse consultaFactura(FacturaRequest facturaRequest) throws DomainExceptionCuentaNoExiste;

    @Recover
    void recover(DomainExceptionCuentaNoExiste e, String sql);
}
