package com.serviciudad.controller;


import com.serviciudad.exception.DomainExceptionCuentaNoExiste;
import com.serviciudad.exception.DomainExceptionPlaceToPay;
import com.serviciudad.model.ClientResponse;
import com.serviciudad.model.FacturaRequest;
import com.serviciudad.service.AuthRecaudoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins  = "*" )
@RequestMapping("/api")
public final class SessionController {
    @Autowired
    private AuthRecaudoService authService;


    @RequestMapping(value = "/session", method = RequestMethod.POST)
    public ResponseEntity<ClientResponse> session(@RequestBody FacturaRequest facturaRequest) throws DomainExceptionPlaceToPay {
            return ResponseEntity.ok().body(authService.auth(facturaRequest));
    }
}