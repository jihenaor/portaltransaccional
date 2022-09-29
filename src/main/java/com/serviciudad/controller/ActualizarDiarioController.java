package com.serviciudad.controller;

import com.serviciudad.service.ErrorService;
import com.serviciudad.service.FacturaEvertecService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public final class ActualizarDiarioController {
    @Autowired
    private FacturaEvertecService facturaService;

    @Autowired
    private ErrorService errorService;

    @RequestMapping(value = "/actualizarpendientes", method = RequestMethod.GET)
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<String> actualizardiario() {

        try {
            return ResponseEntity.ok().body(facturaService.seleccionarPagosPendientes() + "");
        } catch (Exception e) {
            errorService.save(e);
            return ResponseEntity.internalServerError().body(null);
        }
    }
}