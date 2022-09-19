package com.serviciudad.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serviciudad.constantes.Constantes;
import com.serviciudad.entity.AuthModel;
import com.serviciudad.entity.CronModel;
import com.serviciudad.exception.DomainExceptionCuentaNoExiste;
import com.serviciudad.exception.DomainExceptionNoEncontradoRequestId;
import com.serviciudad.model.*;
import com.serviciudad.modelpago.PagoResponse;
import com.serviciudad.modelpago.RespuestaResponse;
import com.serviciudad.repository.AuthRepository;
import com.serviciudad.repository.CronRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public final class ConsultaExisteRecaudoSicepService {

    @Autowired
    private ErrorService errorService;

    @Value("${url_recaudo}")
    private String URL_RECAUDO;


    public String existePagoEnBaseRecaudo(String cuenta, String factura) {
        WebClient webClient = WebClient.create(URL_RECAUDO);
        String existeFactura;

        try {
            existeFactura = webClient.get()
                    .uri("/rec/consultarecaudosicep/" + cuenta +"/0/" + factura )
                    .exchange()
                    .block()
                    .bodyToMono(String.class)
                    .block();

        } catch (Exception e) {
            errorService.save(e, "", "Consultando consultarecaudosicep");
            throw e;
        }
        return existeFactura;
    }
}
