package com.serviciudad.controller;

import com.serviciudad.service.ConsultarExisteRecaudoService;
import com.serviciudad.service.ErrorService;
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
public final class ConsultarPagoRecaudoController {
    @Autowired
    private ConsultarExisteRecaudoService consultarExisteRecaudoService;

    @Autowired
    private ErrorService errorService;

    @RequestMapping(value = "/consultarpago/{cuenta}/{referencia}/{codigobanco}", method = RequestMethod.GET)
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<String> listarcuentareferencia(
                @PathVariable("cuenta") String cuenta,
                @PathVariable("referencia") String referencia,
                @PathVariable("codigobanco") String codigobanco) {

        try {
            String existe = consultarExisteRecaudoService.existePagoEnBaseRecaudo(
                    cuenta,
                    referencia,
                    codigobanco);
            return ResponseEntity.ok().body(existe);
        } catch (Exception e) {
            errorService.save(e);
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
