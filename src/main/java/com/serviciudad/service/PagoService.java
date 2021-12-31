package com.serviciudad.service;


import com.serviciudad.model.*;
import com.serviciudad.modelpago.PagoResponse;
import com.serviciudad.repository.AuthRepository;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public final class PagoService {
    @Autowired
    AuthRepository authRepository;

    @Autowired
    private FacturaService facturaService;

    @Autowired
    private ErrorService errorService;

    public PagoResponse pagarFactura(PagoRequest pagoRequest) {
        PagoResponse pagoResponse;
        SessionRequest sessionRequest;
        WebClient webClient;
        FacturaResponse facturaResponse;

        try {
            webClient = WebClient.create("https://checkout-test.placetopay.com/api");
        } catch (Exception e) {
            errorService.save(e);
            throw e;
        }

        AuthModel authModel = consulta(pagoRequest);

        AuthRequestInformation authRequestInformation = new AuthRequestInformation(
                    new Auth(authModel.getLogin(),
                                authModel.getTrankey(),
                                authModel.getNonce(),
                                authModel.getSeed())
                        );
        try {
            pagoResponse = webClient.post()
                    .uri("/session/" + authModel.getRequestid())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(Mono.just(authRequestInformation), ClientRequest.class)
                    .retrieve()
                    .bodyToMono(PagoResponse.class)
                    .timeout(Duration.ofSeconds(20))  // timeout
                    .block();
        } catch (Exception e) {
            errorService.save(e);
            e.printStackTrace();
            throw e;
        }

//        update(sessionRequest, clientResponse.getRequestId());

        return pagoResponse;
    }


    private void update(SessionRequest sessionRequest, int requestId) {
        authRepository.save(
                new AuthModel(sessionRequest, requestId));
    }

    public AuthModel consulta(PagoRequest pagoRequest) {
        return authRepository.findByCuentaAndReference(pagoRequest.getCodsuscrip(), pagoRequest.getIdfactura());
    }
}
