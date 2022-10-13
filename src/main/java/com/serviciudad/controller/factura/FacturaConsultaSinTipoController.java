package com.serviciudad.controller.factura;

import com.serviciudad.exception.DomainExceptionCuentaNoExiste;
import com.serviciudad.model.FacturaRequest;
import com.serviciudad.model.FacturaResponse;
import com.serviciudad.service.FacturaEvertecService;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins  = "*" )
@RequestMapping("/api")
public class FacturaConsultaSinTipoController {
    @Autowired
    private FacturaEvertecService facturaService;


    @RequestMapping(value = "/consultafactura", method = RequestMethod.POST)
    @Retry(name = "flightSearch", fallbackMethod = "consultafactura2")
    public ResponseEntity<String> consultafactura(@RequestBody FacturaRequest facturaRequest) throws DomainExceptionCuentaNoExiste {

        try {
            return ResponseEntity.ok().body(facturaService.consultaFactura(facturaRequest));
        } catch (DomainExceptionCuentaNoExiste domainExceptionCuentaNoExiste) {
           throw domainExceptionCuentaNoExiste;
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    public ResponseEntity<String> consultafactura2(@RequestBody FacturaRequest facturaRequest, DomainExceptionCuentaNoExiste domainExceptionCuentaNoExiste) {
        return null;
    }
}