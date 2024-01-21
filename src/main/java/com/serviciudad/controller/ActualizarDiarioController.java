package com.serviciudad.controller;

import com.serviciudad.service.FacturaEvertecService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Slf4j
public final class ActualizarDiarioController {
    @Autowired
    private FacturaEvertecService facturaService;

    @RequestMapping(value = "/actualizarpendientes", method = RequestMethod.GET)
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<String> actualizardiario() {
        return ResponseEntity.ok().body(facturaService.seleccionarPagosPendientes() + "");

    }
}