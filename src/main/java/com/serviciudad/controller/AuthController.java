package com.serviciudad.controller;


import com.serviciudad.model.AuthModel;
import com.serviciudad.model.ClientResponse;
import com.serviciudad.model.FacturaRequest;
import com.serviciudad.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins  = {"https://serviciudadpse.com", "https://serviciudad.gov.co"} )
@RestController
@RequestMapping("/api")
public final class AuthController {
    @Autowired
    private AuthService authService;

    @RequestMapping(value = "/session", method = RequestMethod.POST)
    public ResponseEntity<ClientResponse> session(@RequestBody FacturaRequest facturaRequest) {
        try {
            return ResponseEntity.ok().body(authService.auth(facturaRequest));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @RequestMapping(value = "/listarsessiones", method = RequestMethod.GET)
    public ResponseEntity<List<AuthModel>> listar() {

        try {
            return ResponseEntity.ok().body(authService.listar());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }
}