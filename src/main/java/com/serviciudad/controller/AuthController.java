package com.serviciudad.controller;


import com.serviciudad.exception.DomainExceptionPlaceToPay;
import com.serviciudad.exception.DomainExceptionCuentaNoExiste;
import com.serviciudad.entity.AuthModel;
import com.serviciudad.model.ClientResponse;
import com.serviciudad.model.FacturaRequest;
import com.serviciudad.service.AuthRecaudoService;
import com.serviciudad.service.ErrorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins  = "*" )
@RequestMapping("/api")
public final class AuthController {
    @Autowired
    private AuthRecaudoService authService;

    @Autowired
    private ErrorService errorService;

    @RequestMapping(value = "/listarsessiones", method = RequestMethod.GET)
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<AuthModel>> listar() {

        try {
            return ResponseEntity.ok().body(authService.listar());
        } catch (Exception e) {
            errorService.save(e);
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @RequestMapping(value = "/listarpendientes", method = RequestMethod.GET)
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<AuthModel>> listarpendientes() {

        try {
            return ResponseEntity.ok().body(authService.listarpendientes());
        } catch (Exception e) {
            errorService.save(e);
            return ResponseEntity.internalServerError().body(null);
        }
    }
}