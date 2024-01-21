package com.serviciudad.controller;


import com.serviciudad.compartido.model.ValueStringDomain;
import com.serviciudad.entity.AuthModel;
import com.serviciudad.service.AuthRecaudoListarService;
import com.serviciudad.service.AuthRecaudoService;
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
    private AuthRecaudoListarService authRecaudoListarService;

    @RequestMapping(value = "/listarsessiones/{fecha}", method = RequestMethod.GET)
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<AuthModel>> listar(@PathVariable("fecha") String fecha) {

        return ResponseEntity.ok().body(authRecaudoListarService.listar(new ValueStringDomain(fecha, 10)));
    }

    @RequestMapping(value = "/listarpendientes", method = RequestMethod.GET)
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<AuthModel>> listarpendientes() {
        return ResponseEntity.ok().body(authService.listarpendientes());

    }
}