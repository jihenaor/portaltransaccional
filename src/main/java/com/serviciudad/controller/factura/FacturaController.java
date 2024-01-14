package com.serviciudad.controller.factura;

import com.serviciudad.entity.AuthModel;
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