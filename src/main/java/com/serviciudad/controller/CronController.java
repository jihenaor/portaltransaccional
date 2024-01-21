package com.serviciudad.controller;


import com.serviciudad.entity.CronModel;
import com.serviciudad.service.CronService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public final class CronController {
    @Autowired
    private CronService cronService;

    @RequestMapping(value = "/listarcron", method = RequestMethod.GET)
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<CronModel>> listar() {

        return ResponseEntity.ok().body(cronService.listar());
    }
}