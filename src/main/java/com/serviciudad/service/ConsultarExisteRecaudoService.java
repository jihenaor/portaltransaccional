package com.serviciudad.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serviciudad.compartido.model.ValueStringDomain;
import com.serviciudad.constantes.Constantes;
import com.serviciudad.controller.ActualizarDiarioController;
import com.serviciudad.entity.AuthModel;
import com.serviciudad.entity.CronModel;
import com.serviciudad.entity.IdRecaudoModel;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

@Service
public class ConsultarExisteRecaudoService {

    @Autowired
    private ErrorService errorService;


    @Value("${url_recaudo}")
    private String URL_RECAUDO;


    public ConsultarExisteRecaudoService() {
    }


    public String existePagoEnBaseRecaudo(String cuenta, String factura, String codigoBanco) {
        WebClient webClient = WebClient.create(URL_RECAUDO);
        String existeFactura;

        try {
            existeFactura = webClient.get()
                    .uri("/rec/consulta/" + cuenta +"/" + factura + "/" + codigoBanco)
                    .exchange()
                    .block()
                    .bodyToMono(String.class)
                    .block();

        } catch (Exception e) {
            errorService.save(e, "", "Consultando existe factura " + factura);
            throw e;
        }
        return existeFactura;
    }


}
