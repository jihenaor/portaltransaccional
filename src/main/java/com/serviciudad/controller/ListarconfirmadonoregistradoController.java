package com.serviciudad.controller;

import com.serviciudad.compartido.model.ValueStringDomain;
import com.serviciudad.entity.ValidaciomModel;
import com.serviciudad.service.ListarConfirmadosNoRegistradosService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public final class ListarconfirmadonoregistradoController {
    @Autowired
    private ListarConfirmadosNoRegistradosService listarConfirmadoNoRegistrado;

    @RequestMapping(value = "/listarconfirmadonoregistrado/{fecha}", method = RequestMethod.GET)
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<ValidaciomModel>> listarconfirmadonoregistrado(
            @PathVariable("fecha") String fecha) {
        return ResponseEntity.ok().body(listarConfirmadoNoRegistrado.listarConfirmadoNoRegistrado(new ValueStringDomain(fecha)));
    }
}
