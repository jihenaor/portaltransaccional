package com.serviciudad.controller;

import com.serviciudad.model.HelloResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public final class TestController {
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ResponseEntity<HelloResponse> test() {
        try {
            return ResponseEntity.ok().body(new HelloResponse());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
}