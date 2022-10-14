package com.serviciudad.controller.factura;

import com.serviciudad.exception.DomainExceptionCuentaNoExiste;
import com.serviciudad.model.FacturaRequest;
import com.serviciudad.model.FacturaResponse;
import com.serviciudad.service.FacturaEvertecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins  = "*" )
@RequestMapping("/api")
public final class FacturaConsultaSinTipoController {
    @Autowired
    private FacturaEvertecService facturaService;


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


}