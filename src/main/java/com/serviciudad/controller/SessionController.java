package com.serviciudad.controller;


import com.serviciudad.entity.AuthModel;
import com.serviciudad.exception.DomainExceptionCuentaNoExiste;
import com.serviciudad.exception.DomainExceptionPlaceToPay;
import com.serviciudad.model.ClientResponse;
import com.serviciudad.model.FacturaRequest;
import com.serviciudad.service.AuthRecaudoService;
import com.serviciudad.service.ErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins  = "*" )
@RequestMapping("/api")
public final class SessionController {
    @Autowired
    private AuthRecaudoService authService;

    @Autowired
    private ErrorService errorService;

    @RequestMapping(value = "/session", method = RequestMethod.POST)
    public ResponseEntity<ClientResponse> session(@RequestBody FacturaRequest facturaRequest) {
        try {
            return ResponseEntity.ok().body(authService.auth(facturaRequest));
        } catch (DomainExceptionCuentaNoExiste domainExceptionCuentaNoExiste) {
            return ResponseEntity.noContent().build();
        } catch (DomainExceptionPlaceToPay domainErrorPlaceToPay) {
            return ResponseEntity.internalServerError().body(null);
        } catch (Exception e) {
            errorService.save(e);
            return ResponseEntity.internalServerError().body(null);
        }
    }
}