package com.serviciudad.controller;

import com.serviciudad.entity.AuthModel;
import com.serviciudad.entity.ValidaciomModel;
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
public final class ListarconfirmadonoregistradoController {
    @Autowired
    private FacturaService facturaService;

    @Autowired
    private AuthService authService;

    @Autowired
    private ErrorService errorService;

    @RequestMapping(value = "/listarconfirmadonoregistrado", method = RequestMethod.GET)
    public ResponseEntity<List<ValidaciomModel>> listarconfirmadonoregistrado() {

        try {
            return ResponseEntity.ok().body(authService.listarConfirmadoNoRegistrado());
        } catch (Exception e) {
            errorService.save(e);
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
