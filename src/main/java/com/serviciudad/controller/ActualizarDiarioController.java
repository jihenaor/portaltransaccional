package com.serviciudad.controller;

import com.serviciudad.service.ErrorService;
import com.serviciudad.service.FacturaEvertecService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
public final class ActualizarDiarioController {
    @Autowired
    private FacturaEvertecService facturaService;

    @Autowired
    private ErrorService errorService;

    static final Logger LOGGER = Logger.getLogger(String.valueOf(ActualizarDiarioController.class));

    @RequestMapping(value = "/actualizarpendientes", method = RequestMethod.GET)
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<String> actualizardiario() {

        LOGGER.info("Inicia actualizarpendientes");
        try {
            return ResponseEntity.ok().body(facturaService.seleccionarPagosPendientes() + "");
        } catch (Exception e) {
            LOGGER.info("Error actualizarpendientes " + e.getMessage());
            errorService.save(e);
            return ResponseEntity.internalServerError().body(null);
        }
    }
}