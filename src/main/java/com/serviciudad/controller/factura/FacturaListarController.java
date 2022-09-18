package com.serviciudad.controller.factura;

import com.serviciudad.entity.AuthModel;
import com.serviciudad.service.AuthRecaudoService;
import com.serviciudad.service.ErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins  = "*" )
@RequestMapping("/api")
public final class FacturaListarController {
    @Autowired
    private AuthRecaudoService authRecaudoService;

    @Autowired
    private ErrorService errorService;

    @RequestMapping(value = "/listar", method = RequestMethod.GET)
    public ResponseEntity<List<AuthModel>> listar() {

        try {
            return ResponseEntity.ok().body(authRecaudoService.listar());
        } catch (Exception e) {
            errorService.save(e);
            return ResponseEntity.internalServerError().body(null);
        }
    }
}