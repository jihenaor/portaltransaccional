package com.serviciudad.controller.factura;

import com.serviciudad.model.FacturaResponse;
import com.serviciudad.model.FacturaTipoRequest;
import com.serviciudad.service.FacturaConsultaTipoService;
import com.serviciudad.service.RequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins  = "*" )
@RequestMapping("/api")
public final class FacturaConsultaConTipoController {
    @Autowired
    private FacturaConsultaTipoService facturaConsultaTipoService;

    @Autowired
    RequestService requestService;

    @Operation(summary = "Consulta el estado de una cuenta por tipo de factura")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Cuenta consultada",
                    content = {
                            @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = FacturaResponse.class))
                            }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Ingreso no autorizado - Usuario no valido",
                    content = @Content),
    })

    @RequestMapping(value = "/consultafacturatipo", method = RequestMethod.POST)
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<FacturaResponse> consultafactura(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Permite consultar el saldo de la ultima factura de un tipo especifico para una cuenta",
                    content = @Content(
                            examples = @ExampleObject( name = "usuario",
                                    value = "{\"codsuscrip\": \"808085\", \"tipoFactura\": \"0\", \"numerofactura\": \"271356293\", \"valor\": \"0\"}",
                                    summary = "Ejemplo JSON consulta")
                    )
            )
            @RequestBody FacturaTipoRequest facturaTipoRequest) {

        try {
            FacturaResponse facturaResponse = facturaConsultaTipoService.consultarFacturaTipo(facturaTipoRequest);

            grabarRequest(facturaTipoRequest,
                    facturaResponse.getCodRespuesta().toString(),
                    "D:" + facturaResponse.getDescripcion() +
                            " V: "+ facturaResponse.getFacturavencida(),
                            facturaResponse.getTotalfactura());

            return ResponseEntity.ok().body(facturaResponse);
        } catch (Exception e) {
            grabarRequest(facturaTipoRequest,
                    "ERR",
                    e.getMessage(),
                    0L);

            return ResponseEntity.internalServerError().body(null);
        }
    }

    private void grabarRequest(FacturaTipoRequest facturaTipoRequest,
                               String codigoRespuesta,
                               String comentario,
                               Long total) {
        try {
            requestService.save(facturaTipoRequest.getCodsuscrip() == null ? "N/A" : facturaTipoRequest.getCodsuscrip(),
                    facturaTipoRequest.getNumerofactura(),
                    facturaTipoRequest.getTipoFactura(),
                    "",
                    codigoRespuesta,
                    comentario,
                    total);
        } catch (Exception e2) {

        }
    }
}