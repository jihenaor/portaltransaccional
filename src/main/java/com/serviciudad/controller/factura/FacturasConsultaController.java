package com.serviciudad.controller.factura;

import com.serviciudad.exception.DomainExceptionCuentaNoExiste;
import com.serviciudad.model.FacturaRequest;
import com.serviciudad.models.factura.application.ports.FacturaResponse;
import com.serviciudad.service.FacturaConsultasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins  = "*" )
@RequestMapping("/api")
public final class FacturasConsultaController {
    @Autowired
    private FacturaConsultasService facturaConsultasService;

    @RequestMapping(value = "/consultafacturas", method = RequestMethod.POST)
    public ResponseEntity<List<FacturaResponse>> consultafactura(@RequestBody FacturaRequest facturaRequest) {
        try {
            return ResponseEntity.ok().body(facturaConsultasService.consultarFacturas(facturaRequest));
        } catch (DomainExceptionCuentaNoExiste domainExceptionCuentaNoExiste) {
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
}