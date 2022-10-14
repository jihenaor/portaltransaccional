package com.serviciudad.controller.factura;

import com.serviciudad.compartido.model.ValueStringDomain;
import com.serviciudad.entity.AuthModel;
import com.serviciudad.service.ErrorService;
import com.serviciudad.service.FacturaEvertecService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins  = "*" )
@RequestMapping("/api")
public final class FacturaController {
    @Autowired
    private FacturaEvertecService facturaService;

    @Autowired
    private ErrorService errorService;

    @RequestMapping(value = "/reprocesar/{clave}/{fecha}", method = RequestMethod.GET)
    @SecurityRequirement(name = "Bearer Authentication")
    public String reprocesar(@PathVariable ("clave") String clave, @PathVariable("fecha") String fecha) {
        if (clave.equals("pepeloco")) {
            try {
                return "Procesados:" + facturaService.seleccionarPagosAprobadosConfirmadosValidar(new ValueStringDomain(fecha, 10));
            } catch (Exception e) {
                errorService.save(e);
                return e.getMessage();
            }
        } else {
            return "clave invalida";
        }
    }

    @RequestMapping(value = "/validarevertec/{numerofactura}/{clave}", method = RequestMethod.GET)
    @SecurityRequirement(name = "Bearer Authentication")
    public List<AuthModel> validarevertec(@PathVariable ("numerofactura") String numerofactura,
                                 @PathVariable ("clave") String clave) {
        if (clave.equals("pepeloco")) {
            return facturaService.validarFactura(numerofactura);
        } else {
            return new ArrayList<>();
        }
    }
}