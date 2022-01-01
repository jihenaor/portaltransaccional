package com.serviciudad.controller;

import com.serviciudad.service.ErrorService;
import com.serviciudad.model.ErrorModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public final class ErrorController {
    @Autowired
    private ErrorService errorService;

    @RequestMapping(value = "/listarerrores", method = RequestMethod.GET)
    public ResponseEntity<List<ErrorModel>> listar() {

        try {
            return ResponseEntity.ok().body(errorService.listar());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @RequestMapping(value = "/borrarerrores", method = RequestMethod.GET)
    public ResponseEntity<String> borrar() {

        try {
            errorService.borrar();
            return ResponseEntity.ok().body("OK");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }
}