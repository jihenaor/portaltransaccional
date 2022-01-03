package com.serviciudad.controller;

import com.serviciudad.exception.DomainExceptionCuentaNoExiste;
import com.serviciudad.model.AuthModel;
import com.serviciudad.model.FacturaRequest;
import com.serviciudad.model.FacturaResponse;
import com.serviciudad.service.AuthService;
import com.serviciudad.service.ErrorService;
import com.serviciudad.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public final class FacturaController {
    @Autowired
    private FacturaService facturaService;

    @Autowired
    private AuthService authService;

    @Autowired
    private ErrorService errorService;

    @RequestMapping(value = "/consultafactura", method = RequestMethod.POST)
    public ResponseEntity<FacturaResponse> consultafactura(@RequestBody FacturaRequest facturaRequest) {

        try {
            return ResponseEntity.ok().body(facturaService.consultaFactura(facturaRequest));
        } catch (DomainExceptionCuentaNoExiste domainExceptionCuentaNoExiste) {
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @RequestMapping(value = "/listar", method = RequestMethod.GET)
    public ResponseEntity<List<AuthModel>> listar() {

        try {
            return ResponseEntity.ok().body(authService.listar());
        } catch (Exception e) {
            errorService.save(e);
            return ResponseEntity.internalServerError().body(null);
        }
    }
}