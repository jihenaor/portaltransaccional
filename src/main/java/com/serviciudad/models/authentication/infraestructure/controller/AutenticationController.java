package com.serviciudad.models.authentication.infraestructure.controller;

import com.serviciudad.compartido.exceptions.ApiUnauthorized;
import com.serviciudad.model.JwtResponse;
import com.serviciudad.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1.0")
public class AutenticationController {
    @Autowired
    private AuthService authenticationService;

    @Operation(summary = "Autentica un usuario")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Autentica un usuario asignandole un token (JWT) que le permite consumir las API's",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JwtResponse.class)) }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Ingreso no autorizado - Usuario no valido",
                    content = @Content),
    })

    @RequestMapping(value = "/oauth/cliente_credential/accesstoken", method = RequestMethod.POST)
    public ResponseEntity<Object> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Permite validar si un usuario tiene autorización para usar las APIS y obtiene como respuesta un JWT",
                    content = @Content(
                            examples = @ExampleObject( name = "usuario",
                                    value = "{\"usuario\": \"codigo-usuario-recaudador\", \"contrasena\": \"Ka38%C#u6b$k\"}",
                                    summary = "Ejemplo JSON autenticacion")
                    )
            )
            @RequestBody Request request

    ) throws ApiUnauthorized {
        System.out.println("Autenticando");
        return ResponseEntity.ok(
                authenticationService.login(
                        request.getUsuario(),
                        request.getContrasena()
                )
        );
    }
}


@Getter
@Setter
@NoArgsConstructor
final class Request {
    @Schema(description = "Codigo del usuario", example = "codigo-usuario-recaudador")
    private String usuario;
    @Schema(description = "Contraseña", example = "Ka38%C#u6b$k")
    private String contrasena;
}
