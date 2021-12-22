package com.serviciudad.portaltransaccional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public final class AuthController {
    @Autowired
    private AuthService authService;

    @RequestMapping(value = "/session", method = RequestMethod.POST)
    public ResponseEntity<ClientResponse> session(@RequestBody FacturaRequest facturaRequest) {
        try {
            return ResponseEntity.ok().body(authService.auth(facturaRequest));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }
}