package com.serviciudad.controller;

import com.serviciudad.service.ConsultarExisteRecaudoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public final class ConsultarPagoRecaudoController {
    @Autowired
    private ConsultarExisteRecaudoService consultarExisteRecaudoService;

    @RequestMapping(value = "/consultarpago/{cuenta}/{referencia}/{codigobanco}", method = RequestMethod.GET)
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<String> listarcuentareferencia(
                @PathVariable("cuenta") String cuenta,
                @PathVariable("referencia") String referencia,
                @PathVariable("codigobanco") String codigobanco) {

        String existe = consultarExisteRecaudoService.existePagoEnBaseRecaudo(
                cuenta,
                referencia,
                codigobanco);
        return ResponseEntity.ok().body(existe);
    }
}
