package com.serviciudad.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public final class ConsultaExisteRecaudoSicepService {

    @Value("${url_recaudo}")
    private String URL_RECAUDO;

    public String existePagoEnBaseRecaudo(String cuenta, String factura) {
        WebClient webClient = WebClient.create(URL_RECAUDO);

        return webClient.get()
                    .uri("/rec/consultarecaudosicep/" + cuenta +"/0/" + factura )
                    .exchange()
                    .block()
                    .bodyToMono(String.class)
                    .block();
    }
}
