package com.serviciudad.controller;

import com.serviciudad.entity.AuthModel;
import com.serviciudad.entity.CuentaModel;
import com.serviciudad.entity.ReferenciaModel;
import com.serviciudad.service.ListarCuantaReferenciaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public final class ListarcuentareferenciaController {
    @Autowired
    private ListarCuantaReferenciaService listarCuantaReferenciaService;

    @RequestMapping(value = "/listarcuentareferencia/{cuenta}/{referencia}", method = RequestMethod.GET)
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<AuthModel>> listarcuentareferencia(
                @PathVariable("cuenta") String cuenta,
                @PathVariable("referencia") String referencia) {

        return ResponseEntity.ok().body(listarCuantaReferenciaService.listar(
                new CuentaModel(cuenta),
                new ReferenciaModel(referencia)));
    }
}
