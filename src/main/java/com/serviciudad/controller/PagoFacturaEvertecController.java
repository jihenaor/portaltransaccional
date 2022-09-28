package com.serviciudad.controller;

import com.serviciudad.model.PagoEvertecRequest;
import com.serviciudad.modelpago.RespuestaResponse;
import com.serviciudad.service.ErrorService;
import com.serviciudad.service.FacturaEvertecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public final class PagoFacturaEvertecController {
    @Autowired
    private FacturaEvertecService facturaService;

    @Autowired
    private ErrorService errorService;

    @RequestMapping(value = "/pagarfactura", method = RequestMethod.POST)
    public ResponseEntity<RespuestaResponse> pagarfactura(@RequestBody PagoEvertecRequest pagoRequest) {
        try {
            return ResponseEntity.ok().body(facturaService.pagarFactura(pagoRequest, false, Optional.empty()));
        } catch (Exception e) {
            errorService.save(e);
            return ResponseEntity.internalServerError().body(null);
        }
    }
}