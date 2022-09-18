package com.serviciudad.controller;

import com.serviciudad.service.FacturaEvertecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public final class ActualizarDiarioController {
    @Autowired
    private FacturaEvertecService facturaService;

    @RequestMapping(value = "/actualizardiario/{clave}", method = RequestMethod.GET)
    public int actualizardiario(@PathVariable ("clave") String clave) {
        if (clave.equals("pepeloco")) {
            return facturaService.seleccionarPagosPendientes();
        } else {
            return -1;
        }
    }
}