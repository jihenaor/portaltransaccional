package com.serviciudad.controller.test;

import com.serviciudad.model.HealthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public final class HealthController {
    @RequestMapping(value = "/health", method = RequestMethod.GET)
    public ResponseEntity<HealthResponse> test() {
        try {
            return ResponseEntity.ok().body(new HealthResponse());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
}