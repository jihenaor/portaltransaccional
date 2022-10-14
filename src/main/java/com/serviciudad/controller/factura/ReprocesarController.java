package com.serviciudad.controller.factura;

import com.serviciudad.compartido.model.ValueStringDomain;
import com.serviciudad.service.ErrorService;
import com.serviciudad.service.FacturaEvertecService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins  = "*" )
@RequestMapping("/api")
public final class ReprocesarController {
    @Autowired
    private FacturaEvertecService facturaService;

    @Autowired
    private ErrorService errorService;

    @Autowired
    private Environment env;

    @RequestMapping(value = "/reprocesar/{clave}/{fecha}", method = RequestMethod.GET)
    @SecurityRequirement(name = "Bearer Authentication")
    public String reprocesar(@PathVariable ("clave") String clave, @PathVariable("fecha") String fecha) {
        if (clave.equals("pepeloco")) {
            try {
                return "Procesados:" + facturaService.seleccionarPagosAprobadosConfirmadosValidar(
                        new ValueStringDomain(fecha, 10), env.getProperty("CODIGOBANCOPLACETOPAY"));
            } catch (Exception e) {
                errorService.save(e);
                return e.getMessage();
            }
        } else {
            return "clave invalida";
        }
    }
}