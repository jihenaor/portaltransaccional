package com.serviciudad.models.factura.application.ConsultaFacturaTipo;

import com.serviciudad.compartido.exceptions.BusinessException;
import com.serviciudad.model.*;
import com.serviciudad.models.factura.application.ports.FacturaResponse;
import com.serviciudad.models.factura.infraestructure.web.adapters.FacturaTipoRequest;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

@Service
@Slf4j
public final class ConsultaFacturaTipoService {

    @Value("${url_sicesp}")
    private String URL_SICESP;

    public FacturaResponse consultarFacturaTipo(FacturaTipoRequest facturaTipoRequest) throws BusinessException {
        FacturaResponse facturaResponse = getFacturaResponse(facturaTipoRequest);

        validarFacturaVencida(facturaTipoRequest, facturaResponse);

        return facturaResponse;
    }

    private FacturaResponse getFacturaResponse(FacturaTipoRequest facturaTipoRequest) {
        FacturaResponse facturaResponse;
        WebClient webClient = WebClient.create(URL_SICESP);
        String URI = "/api/consultafacturatipo";

        facturaResponse = webClient.post()
                .uri(URI)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(facturaTipoRequest), FacturaRequest.class)
                .retrieve()
                .bodyToMono(FacturaResponse.class)
                .timeout(Duration.ofSeconds(20))  // timeout
                .block();
        return facturaResponse;
    }

    private static void validarFacturaVencida(FacturaTipoRequest facturaTipoRequest, FacturaResponse facturaResponse) {
        assert facturaResponse != null;
        if (facturaResponse.getFechapago() != null && facturaResponse.getFechapago().length() == 10) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            int fechaPago = Integer.parseInt(facturaResponse.getFechapago().replace("-", ""));
            int fechaActual = Integer.parseInt(dateFormat.format(new Date()));

            if (facturaTipoRequest.getTipoFactura().equals("0")
                    || facturaTipoRequest.getTipoFactura().equals("00")) {
                facturaResponse.setFacturavencida(fechaActual > fechaPago ? "S" : "N");
            } else {
                facturaResponse.setFacturavencida("N");
            }
        } else {
            facturaResponse.setFacturavencida("N");
        }

        facturaResponse.setTipofact(Integer.parseInt(facturaTipoRequest.getTipoFactura()));
    }

}
