package com.serviciudad.models.health.infraestructure.controller;

import com.serviciudad.model.HealthResponse;
import com.serviciudad.model.JwtResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Actuator de Spring Boot: S
@RestController
@RequestMapping("/api")
public final class HealthController {
    @Operation(summary = "Check health of the application")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved health status",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JwtResponse.class)) }),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error",
                    content = @Content),
    })


    @GetMapping(value = "/health")
    public ResponseEntity<HealthResponse> test() {

        return ResponseEntity.ok().body(new HealthResponse());
    }
}