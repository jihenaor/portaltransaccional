package com.serviciudad.portaltransaccional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


import java.time.Duration;
import java.util.*;

@Service
public final class FacturaService {
    @Autowired
    AuthRepository authRepository;

    public FacturaResponse consultaFactura(FacturaRequest facturaRequest) {
        WebClient webClient = WebClient.create("http://192.168.100.72:8080/recaudos/api");

        return webClient.post()
                .uri("/rec")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(facturaRequest), FacturaRequest.class)
                .retrieve()
                .bodyToMono(FacturaResponse.class)
                .timeout(Duration.ofSeconds(20))  // timeout
                .block();
    }

    public List<AuthModel> listar() {
        return (List<AuthModel>) authRepository.findAll();
    }
}
