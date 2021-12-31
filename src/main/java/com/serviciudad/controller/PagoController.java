package com.serviciudad.controller;

import com.serviciudad.model.PagoRequest;
import com.serviciudad.modelpago.PagoResponse;
import com.serviciudad.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins  = {"http://serviciudadpse.com"} )
@RestController
@RequestMapping("/api")
public final class PagoController {
    @Autowired
    private PagoService pagoService;

    @RequestMapping(value = "/pagarfactura", method = RequestMethod.POST)
    public ResponseEntity<PagoResponse> pagarfactura(@RequestBody PagoRequest pagoRequest) {

        try {
            return ResponseEntity.ok().body(pagoService.pagarFactura(pagoRequest));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }
}