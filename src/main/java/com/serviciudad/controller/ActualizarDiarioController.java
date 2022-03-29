package com.serviciudad.controller;

import com.serviciudad.entity.AuthModel;
import com.serviciudad.exception.DomainExceptionCuentaNoExiste;
import com.serviciudad.model.FacturaRequest;
import com.serviciudad.model.FacturaResponse;
import com.serviciudad.service.AuthService;
import com.serviciudad.service.ErrorService;
import com.serviciudad.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public final class ActualizarDiarioController {
    @Autowired
    private FacturaService facturaService;

    @RequestMapping(value = "/actualizardiario/{clave}", method = RequestMethod.GET)
    public int actualizardiario(@PathVariable ("clave") String clave) {
        if (clave.equals("pepeloco")) {
            return facturaService.seleccionarPagosPendientes();
        } else {
            return -1;
        }
    }
}