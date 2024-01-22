package com.serviciudad.models.factura.infraestructure.web;

import com.serviciudad.compartido.exceptions.BusinessException;
import com.serviciudad.models.factura.application.ConsultaFacturaTipo.ConsultaFacturaTipoQuery;
import com.serviciudad.models.factura.application.ports.FacturaResponse;
import com.serviciudad.models.factura.infraestructure.web.adapters.FacturaTipoRequest;
import com.serviciudad.models.factura.application.ConsultaFacturaTipo.ConsultaFacturaTipoService;
import com.serviciudad.service.RequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins  = "*" )
@RequestMapping("/api")
@Slf4j
public final class FacturaConsultaConTipoController {
    @Autowired
    private ConsultaFacturaTipoQuery consultaFacturaTipoQuery;

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
            @Valid @RequestBody FacturaTipoRequest facturaTipoRequest) throws BusinessException {

            FacturaResponse facturaResponse = consultaFacturaTipoQuery.handle(facturaTipoRequest);

//            log.info();
/*
            grabarRequest(facturaTipoRequest,
                    facturaResponse.getCodRespuesta().toString(),
                    "D:" + facturaResponse.getDescripcion() +
                            " V: "+ facturaResponse.getFacturavencida(),
                            facturaResponse.getTotalfactura());
*/
            return ResponseEntity.ok().body(facturaResponse);

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