package com.serviciudad.controller.authentication;

import com.serviciudad.compartido.exceptions.ApiUnauthorized;
import com.serviciudad.controller.test.ResponseVersion;
import com.serviciudad.model.JwtResponse;
import com.serviciudad.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.media.SchemaProperties;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
                    description = "Permite validar si un usuario tiene autorización para usar las APIS y obtiene con respuesta un JWT",
                    content = @Content(
                            examples = @ExampleObject( name = "usuario",
                                    value = "{\"usuario\": \"recaudador\", \"contrasena\": \"Ka38%C#u6b$k\"}",
                                    summary = "Ejemplo JSON autenticacion")
                    )
            )
            @RequestBody Request request

    ) throws ApiUnauthorized {

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
    private String usuario;
    private String contrasena;
}