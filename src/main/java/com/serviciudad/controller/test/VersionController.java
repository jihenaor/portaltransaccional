package com.serviciudad.controller.test;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/api")
public class VersionController {
    @Operation(summary = "Obtiene la versión de la api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Obtiene el número de la ultima version de la api de recaudo",
                    content = { @Content(
                                        mediaType = "application/json",
                                        examples = @ExampleObject( name = "usuario",
                                                value = "{\"version\": \"v1.0\"}",
                                                summary = "Ejemplo de respuesta"),
                                        schema = @Schema(implementation = ResponseVersion.class))
                            }),
            @ApiResponse(responseCode = "401", description = "Usuario no autorizado",
                    content = @Content(

                    )),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(path = "/version")
    public ResponseEntity<ResponseVersion> version() {
        return ResponseEntity.ok(ResponseVersion.builder()
                                                .version("v1.0")
                                                .build());
    }
}
