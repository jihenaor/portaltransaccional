package com.serviciudad.controller;

import com.serviciudad.entity.ValidaciomModel;
import com.serviciudad.service.AuthRecaudoService;
import com.serviciudad.service.ErrorService;
import com.serviciudad.service.FacturaEvertecService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public final class ListarconfirmadonoregistradoController {
    @Autowired
    private AuthRecaudoService authService;

    @Autowired
    private ErrorService errorService;

    @RequestMapping(value = "/listarconfirmadonoregistrado", method = RequestMethod.GET)
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<ValidaciomModel>> listarconfirmadonoregistrado() {

        try {
            return ResponseEntity.ok().body(authService.listarConfirmadoNoRegistrado());
        } catch (Exception e) {
            errorService.save(e);
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
