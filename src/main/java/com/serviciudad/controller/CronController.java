package com.serviciudad.controller;


import com.serviciudad.entity.AuthModel;
import com.serviciudad.entity.CronModel;
import com.serviciudad.exception.DomainExceptionCuentaNoExiste;
import com.serviciudad.model.ClientResponse;
import com.serviciudad.model.FacturaRequest;
import com.serviciudad.service.AuthService;
import com.serviciudad.service.CronService;
import com.serviciudad.service.ErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public final class CronController {
    @Autowired
    private CronService cronService;

    @Autowired
    private ErrorService errorService;

    @RequestMapping(value = "/listarcron", method = RequestMethod.GET)
    public ResponseEntity<List<CronModel>> listar() {

        try {
            return ResponseEntity.ok().body(cronService.listar());
        } catch (Exception e) {
            errorService.save(e);
            return ResponseEntity.internalServerError().body(null);
        }
    }
}