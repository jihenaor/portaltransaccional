package com.serviciudad.controller;

import com.serviciudad.model.PagoRequest;
import com.serviciudad.modelpago.PagoResponse;
import com.serviciudad.service.ErrorService;
import com.serviciudad.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public final class PagoController {
    @Autowired
    private FacturaService facturaService;

    @Autowired
    private ErrorService errorService;

    @RequestMapping(value = "/pagarfactura", method = RequestMethod.POST)
    public ResponseEntity<PagoResponse> pagarfactura(@RequestBody PagoRequest pagoRequest) {

        try {
            return ResponseEntity.ok().body(facturaService.pagarFactura(pagoRequest, false));
        } catch (Exception e) {
            errorService.save(e);
            return ResponseEntity.internalServerError().body(null);
        }
    }
}