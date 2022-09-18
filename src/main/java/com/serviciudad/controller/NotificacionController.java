package com.serviciudad.controller;

import com.serviciudad.exception.DomainExceptionNoEncontradoRequestId;
import com.serviciudad.model.NotificacionRequest;
import com.serviciudad.service.ErrorService;
import com.serviciudad.service.FacturaEvertecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins  = "*" )
@RequestMapping("/api")
public final class NotificacionController {
    @Autowired
    private FacturaEvertecService facturaService;

    @Autowired
    private ErrorService errorService;

    @RequestMapping(value = "/notificacion", method = RequestMethod.POST)
    public ResponseEntity notificarTransaccion(@RequestBody NotificacionRequest notificacionRequest) {
        try {
            facturaService.notificarTransaccion(notificacionRequest);
            return ResponseEntity.ok().build();
        } catch (DomainExceptionNoEncontradoRequestId domainExceptionNoEncontradoRequestId) {
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            errorService.save(e);
            return ResponseEntity.internalServerError().body(null);
        }
    }
}