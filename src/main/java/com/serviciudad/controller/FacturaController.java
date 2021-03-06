package com.serviciudad.controller;

import com.serviciudad.entity.ValidaciomModel;
import com.serviciudad.exception.DomainExceptionCuentaNoExiste;
import com.serviciudad.entity.AuthModel;
import com.serviciudad.model.FacturaRequest;
import com.serviciudad.model.FacturaResponse;
import com.serviciudad.service.AuthService;
import com.serviciudad.service.ErrorService;
import com.serviciudad.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins  = "*" )
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

    @RequestMapping(value = "/reprocesar/{clave}", method = RequestMethod.GET)
    public String reprocesar(@PathVariable ("clave") String clave) {
        if (clave.equals("pepeloco")) {
            try {
                return "Procesados:" + facturaService.seleccionarPagosAprobadosConfirmadosValidar();
            } catch (Exception e) {
                errorService.save(e);
                return e.getMessage();
            }
        } else {
            return "clave invalida";
        }
    }

    @RequestMapping(value = "/reprocesaraprobadopendiente/{clave}", method = RequestMethod.GET)
    public String reprocesaraprobadopendiente(@PathVariable ("clave") String clave) {
        if (clave.equals("pepeloco")) {
            try {
                return facturaService.seleccionarPagosAprobadosSinRegistrar();
            } catch (Exception e) {
                errorService.save(e);
                return e.getMessage();
            }
        } else {
            return "clave invalida";
        }
    }

    @RequestMapping(value = "/validarevertec/{numerofactura}/{clave}", method = RequestMethod.GET)
    public List<AuthModel> validarevertec(@PathVariable ("numerofactura") String numerofactura,
                                 @PathVariable ("clave") String clave) {
        if (clave.equals("pepeloco")) {
            return facturaService.validarFactura(numerofactura);
        } else {
            return new ArrayList<>();
        }
    }
}