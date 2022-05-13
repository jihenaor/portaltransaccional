package com.serviciudad.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public final class TestrecaudoService {
    @Autowired
    private ErrorService errorService;

    @Autowired
    private UtilService utilService;


    private final String URL_RECAUDO = "http://192.168.100.72:8080/recaudos/api";


    public String testRecaudo(String msg) {
        WebClient webClient = WebClient.create(URL_RECAUDO);
        String respuesta = "";

        try {
            respuesta = webClient.get()
                    .uri("/rec/" + msg)
                    .exchange()
                    .block()
                    .bodyToMono(String.class)
                    .block();

        } catch (Exception e) {
            respuesta = e.getMessage();
            errorService.save(e, "", "Testing recaudo");
            throw e;
        }
        return respuesta;
    }
}
