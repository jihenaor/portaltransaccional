package com.serviciudad.controller.factura;

import com.serviciudad.entity.AuthModel;
import com.serviciudad.service.ErrorService;
import com.serviciudad.service.ListarAprobadosNoConfirmadosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins  = "*" )
@RequestMapping("/api")
public final class FacturaListarAprobadoNoConfirmadoController {
    @Autowired
    private ListarAprobadosNoConfirmadosService listarAprobadosNoConfirmados;

    @Autowired
    private ErrorService errorService;

    @Operation(summary = "Consulta recaudos en estado: APPROVED, pagoconfirmado: N")
    @RequestMapping(value = "/listaraprobadosnoconfirmados", method = RequestMethod.GET)
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<AuthModel>> listar() {

        try {
            return ResponseEntity.ok().body(listarAprobadosNoConfirmados.listarAprobadosNoConfirmados());
        } catch (Exception e) {
            errorService.save(e);
            return ResponseEntity.internalServerError().body(null);
        }
    }
}