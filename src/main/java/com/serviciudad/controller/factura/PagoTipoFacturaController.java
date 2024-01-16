package com.serviciudad.controller.factura;

import com.serviciudad.model.FacturaResponse;
import com.serviciudad.model.PagoFacturaEvertecRequest;
import com.serviciudad.model.PagoFacturaResponse;
import com.serviciudad.model.PagoTipoFacturaRequest;
import com.serviciudad.service.ErrorService;
import com.serviciudad.service.MyService;
import com.serviciudad.service.PagarTipoFacturaService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public final class PagoTipoFacturaController {
    @Autowired
    private PagarTipoFacturaService pagarTipoFacturaService;

    @Autowired
    RequestService requestService;

    @Autowired
    private ErrorService errorService;

    private final String KEY = "RECTIPO-";

    @Autowired
    private MyService myService;
    @Operation(summary = "Registrar pago por tipo de factura")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Registro exitoso",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PagoFacturaResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = "{\"codigoRespuesta\": \"1\", \"comentario\": \"Exito\", \"numerofactura\": \"123456\"}"
                                            )
                                    }
                            )
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Ingreso no autorizado - Usuario no valido",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PagoFacturaResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = "{\"codigoRespuesta\": \"0\", \"comentario\": \"error\", \"numerofactura\": \"123456\"}"
                                                    )
                                    }
                            )
                    }),
    })

    @RequestMapping(value = "/pagartipofactura", method = RequestMethod.POST)
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<PagoFacturaResponse> pagarfactura(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Registra el pago de una factura",
                    content = @Content(
                            examples = {
                                    @ExampleObject(name = "Valor recaudo invalido",
                                                   value = "{\"codsuscrip\": \"810056\", \"numerofactura\": \"654321\", \"tipoFactura\": \"00\", \"banco\": \"0\", \"total\": 20000, \"fecha\": \"AAAA-MM-DD hh:mm:ss\"}",
                                                   summary = "Se espera error por valor del recaudo invalido"),
                                    @ExampleObject(name = "No coincide numero de la ultima factura",
                                            value = "{\"codsuscrip\": \"810056\", \"numerofactura\": \"654321\", \"tipoFactura\": \"00\", \"banco\": \"0\", \"total\": 30000, \"fecha\": \"AAAA-MM-DD hh:mm:ss\"}",
                                            summary = "Se espera error por No coincide numero de la ultima factura"),
                                    @ExampleObject(name = "Supera fecha limite de pago",
                                            value = "{\"codsuscrip\": \"9579701212\", \"numerofactura\": \"654321\", \"tipoFactura\": \"00\", \"banco\": \"0\", \"total\": 20000, \"fecha\": \"AAAA-MM-DD hh:mm:ss\"}",
                                            summary = "Se espera error por superar la fecha limite de pago"),
                                    @ExampleObject(name = "Recaudo valido",
                                            value = "{\"codsuscrip\": \"810056\", \"numerofactura\": \"123456\", \"tipoFactura\": \"00\", \"banco\": \"0\", \"total\": 30000, \"fecha\": \"AAAA-MM-DD hh:mm:ss\"}",
                                            summary = "Se espera recaudo valido"),
                            }
                    )
            )

            @RequestBody PagoTipoFacturaRequest pagoTipoFacturaRequest) {

        if (myService.existeLlave(getKey(pagoTipoFacturaRequest.getNumerofactura()))) {
            errorService.save(new Exception("El id proceso ya esta en curso factura" + pagoTipoFacturaRequest.getNumerofactura()));
            return ResponseEntity.internalServerError().build();
        }

        try {
            PagoFacturaResponse pagoFacturaResponse = pagarTipoFacturaService.enviarPago(pagoTipoFacturaRequest);
            grabarRequest(pagoTipoFacturaRequest,
                    pagoFacturaResponse.getCodigoRespuesta(),
                    pagoFacturaResponse.getComentario());

            return ResponseEntity.ok().body(pagoFacturaResponse);
        } catch (Exception e) {
            System.out.println("Err pagartipofactura");
            try {
                grabarRequest(pagoTipoFacturaRequest,
                        "ERR",
                        e.getMessage());
            } catch (Exception e2) {

            }
            errorService.save(e);
            return ResponseEntity.internalServerError().body(null);
        }
    }

    private void grabarRequest(PagoTipoFacturaRequest pagoTipoFacturaRequest,
                               String codigoRespuesta,
                               String comentario) {
        try {
            requestService.save(pagoTipoFacturaRequest.getCodsuscrip(),
                    pagoTipoFacturaRequest.getNumerofactura(),
                    pagoTipoFacturaRequest.getTipoFactura(),
                    pagoTipoFacturaRequest.getBanco(),
                    codigoRespuesta,
                    comentario,
                    pagoTipoFacturaRequest.getTotal()
                );
        } catch (Exception e2) {

        }
    }

    private String getKey(String id) {
        return KEY.concat(id);
    }
}