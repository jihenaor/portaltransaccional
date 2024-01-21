package com.serviciudad.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public final class TestrecaudoService {
    @Value("${url_recaudo}")
    private String URL_RECAUDO;

    public String testRecaudo(String msg) {
        WebClient webClient = WebClient.create(URL_RECAUDO);
        String respuesta = "";

        respuesta = webClient.get()
                .uri("/rec/" + msg)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .block()
                .bodyToMono(String.class)
                .block();

/*
        return webClient.post()
                .uri("/api/consultafacturatipo")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(facturaRequest), FacturaRequest.class)
                .retrieve()
                .bodyToMono(Respuestafactura.class)
                .timeout(Duration.ofSeconds(20))  // timeout
                .block();
  */

        return respuesta;
    }
}
