package com.serviciudad.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ConsultarExisteRecaudoService {
    @Value("${url_recaudo}")
    private String URL_RECAUDO;


    public ConsultarExisteRecaudoService() {
    }


    public String existePagoEnBaseRecaudo(String cuenta, String factura, String codigoBanco) {
        WebClient webClient = WebClient.create(URL_RECAUDO);

        return webClient.get()
                    .uri("/rec/consulta/" + cuenta +"/" + factura + "/" + codigoBanco)
                    .exchange()
                    .block()
                    .bodyToMono(String.class)
                    .block();
    }
}
