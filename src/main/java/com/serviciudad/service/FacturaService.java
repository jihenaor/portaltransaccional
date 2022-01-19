package com.serviciudad.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serviciudad.constantes.Constantes;
import com.serviciudad.exception.DomainExceptionCuentaNoExiste;
import com.serviciudad.model.*;
import com.serviciudad.modelpago.PagoResponse;
import com.serviciudad.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;


import java.time.Duration;
import java.util.Base64;
import java.util.List;

@Service
public final class FacturaService {
    @Autowired
    AuthRepository authRepository;

    @Autowired
    private ErrorService errorService;

    public FacturaResponse consultaFactura(FacturaRequest facturaRequest) throws DomainExceptionCuentaNoExiste {
        WebClient webClient = WebClient.create("http://192.168.100.72:8080/recaudos/api");
        FacturaResponse facturaResponse;
        AuthModel authModel;
        PagoResponse pagoResponse;

        try {
            facturaResponse = webClient.post()
                    .uri("/rec")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(Mono.just(facturaRequest), FacturaRequest.class)
                    .retrieve()
                    .bodyToMono(FacturaResponse.class)
                    .timeout(Duration.ofSeconds(20))  // timeout
                    .block();
        } catch (Exception e) {
            errorService.save(e, "", "Consultando la factura");
            throw e;
        }

        if (facturaResponse.getCodRespuesta() == 1) {
            throw new DomainExceptionCuentaNoExiste();
        }

        try {
            authModel = authRepository.findByCuentaAndReference(
                                                facturaResponse.getCuenta(),
                                                facturaResponse.getIdfactura());
        } catch (Exception e) {
            throw e;
        }

        try {
            if (authModel != null) {
                if (Constantes.ESTADO_PENDIENTE.equals(authModel.getEstado())) {
                    pagoResponse = consultarEstadoPago(authModel);
                    if (!pagoResponse.getStatus().getStatus().equals(authModel.getEstado())) {
                        authModel.setEstado(pagoResponse.getStatus().getStatus());
                        authRepository.save(authModel);
                    }
                    facturaResponse.setStatus(pagoResponse.getStatus().getStatus());
                } else {
                    facturaResponse.setStatus(authModel.getEstado());
                }
            } else {
                facturaResponse.setStatus("NOINICIADO");
            }
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
                authModel.setEstado(Constantes.UNAUTHORIZED);
                authRepository.save(authModel);
                facturaResponse.setStatus("");
            } else {
                throw e;
            }
        } catch (Exception e) {
            errorService.save(e);
            throw e;
        }

        return facturaResponse;
    }

    public PagoResponse consultarEstadoPago(AuthModel authModel) {
        PagoResponse pagoResponse;
        WebClient webClient;
        String json = "";

        try {
            webClient = WebClient.create("https://checkout-test.placetopay.com/api");
        } catch (Exception e) {
            errorService.save(e);
            throw e;
        }
        AuthRequestInformation authRequestInformation = new AuthRequestInformation(
                new Auth(authModel.getLogin(),
                        authModel.getTrankey(),
                        authModel.getNonce(),
                        authModel.getSeed())
        );

        ObjectMapper mapper = new ObjectMapper();
        try {
            json = mapper.writeValueAsString(authRequestInformation);
//            encodedString = Base64.getEncoder().encodeToString(json.getBytes());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

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
            errorService.save(e, json, "consultarEstadoPago");
            throw e;
        }

        return pagoResponse;
    }

    public PagoResponse pagarFactura(PagoRequest pagoRequest) {
        PagoResponse pagoResponse;

        AuthModel authModel = consulta(pagoRequest);

        if (authModel == null) {

        }

        pagoResponse = consultarEstadoPago(authModel);

        if (!authModel.getEstado().equals(pagoResponse.getStatus().getStatus())) {
            authModel.setEstado(pagoResponse.getStatus().getStatus());
            update(authModel);
        }
        return pagoResponse;
    }

    private void update(AuthModel authModel) {
        authRepository.save(authModel);
    }

    public AuthModel consulta(PagoRequest pagoRequest) {
        return authRepository.findByiD(pagoRequest.getId());
    }

    public void seleccionarPagosPendientes() {
        List<AuthModel> authModels = authRepository.findByEstado(Constantes.ESTADO_PENDIENTE);

        authModels.forEach(authModel -> {
            PagoRequest pagoRequest = new PagoRequest(authModel.getId());
            pagarFactura(pagoRequest);
        });
    }
}
