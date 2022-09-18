package com.serviciudad.service;

import com.serviciudad.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public final class PagarTipoFacturaService {
    @Autowired
    private ErrorService errorService;

    @Value("${url_recaudo}")
    private String URL_RECAUDO;

    public PagoFacturaResponse enviarPago(PagoTipoFacturaRequest pagoTipoFacturaRequest) {
        PagoFacturaResponse pagoFacturaResponse;
        WebClient webClient = WebClient.create(URL_RECAUDO);

        try {
            pagoFacturaResponse = webClient.post()
                    .uri("/rec/pagarfacturatipo")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(Mono.just(pagoTipoFacturaRequest), FacturaRequest.class)
                    .retrieve()
                    .bodyToMono(PagoFacturaResponse.class)
                    .timeout(Duration.ofSeconds(20))  // timeout
                    .block();
            if (pagoFacturaResponse.getCodigoRespuesta().equals("1")) {

            } else {
                if (pagoFacturaResponse.getComentario().indexOf("ya ha sido registrada") > 0) {

                }
            }
        } catch (Exception e) {
            errorService.save(e, "", "Registrando pago de factura");
            throw e;
        }
        return pagoFacturaResponse;
    }
}
