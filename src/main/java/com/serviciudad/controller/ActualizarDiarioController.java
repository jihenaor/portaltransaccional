package com.serviciudad.controller;

import com.serviciudad.service.FacturaEvertecService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public final class ActualizarDiarioController {
    @Autowired
    private FacturaEvertecService facturaService;

    @RequestMapping(value = "/actualizarpendientes", method = RequestMethod.GET)
    @SecurityRequirement(name = "Bearer Authentication")
    public int actualizardiario() {

        return facturaService.seleccionarPagosPendientes();

    }
}