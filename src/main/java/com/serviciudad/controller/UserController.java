package com.serviciudad.controller;

import com.serviciudad.controller.test.ResponseVersion;
import com.serviciudad.entity.UserModel;
import com.serviciudad.service.ErrorService;
import com.serviciudad.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public final class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private ErrorService errorService;

    @Operation(summary = "Adiciona usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario creado",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseVersion.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @RequestMapping(value = "/adicionarusuario", method = RequestMethod.POST)
    public ResponseEntity<String> adicionarUsuario(@RequestBody UserModel userModel) {

        try {
            userService.save(userModel);
            return ResponseEntity.ok().body("OK");
        } catch (Exception e) {
            errorService.save(e);
            return ResponseEntity.internalServerError().body(null);
        }
    }
}