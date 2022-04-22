package com.serviciudad.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serviciudad.constantes.Constantes;
import com.serviciudad.entity.AuthModel;
import com.serviciudad.entity.ValidaciomModel;
import com.serviciudad.exception.DomainExceptionPlaceToPay;
import com.serviciudad.exception.DomainExceptionCuentaNoExiste;
import com.serviciudad.model.*;
import com.serviciudad.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;

@Service
public final class AuthService {
    @Autowired
    AuthRepository authRepository;

    @Autowired
    private FacturaService facturaService;

    @Autowired
    private ErrorService errorService;

    @Autowired
    private UtilService utilService;

    @Autowired
    private Environment env;

    public ClientResponse auth(FacturaRequest facturaRequest) throws DomainExceptionCuentaNoExiste, DomainExceptionPlaceToPay {
        ClientResponse clientResponse;
        SessionRequest sessionRequest;
        WebClient webClient;
        String json = "";

        sessionRequest = getSessionRequest(facturaRequest);

        try {
            webClient = WebClient.create(env.getProperty("url"));
        } catch (Exception e) {
            errorService.save(e);
            throw e;
        }

        String id = UUID.randomUUID().toString();
        ClientRequest clientRequest = createRequest(sessionRequest, id);

        sessionRequest.setSeed(clientRequest.getAuth().getSeed());

        ObjectMapper mapper = new ObjectMapper();
        try {
            json = mapper.writeValueAsString(clientRequest);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        try {
            clientResponse = webClient.post()
                    .uri("/session")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(Mono.just(clientRequest), ClientRequest.class)
                    .retrieve()

                    .bodyToMono(ClientResponse.class)
                    .timeout(Duration.ofSeconds(20))  // timeout
                    .block();
        } catch (Exception e) {
            if (e instanceof WebClientResponseException.Unauthorized) {
                errorService.save(json, "auth");
            } else {
                errorService.save(e);
            }
            throw new DomainExceptionPlaceToPay();
        }

        clientResponse = save(sessionRequest, clientResponse, id);

        return clientResponse;
    }

    private SessionRequest getSessionRequest(FacturaRequest facturaRequest) throws DomainExceptionCuentaNoExiste {
        FacturaResponse facturaResponse;
        SessionRequest sessionRequest;
        try {
            facturaResponse = facturaService.consultaFactura(facturaRequest);
        } catch (DomainExceptionCuentaNoExiste domainExceptionCuentaNoExiste) {
            throw domainExceptionCuentaNoExiste;
        } catch (Exception e) {
            errorService.save(e);
            throw e;
        }

        sessionRequest = new SessionRequest(
                facturaRequest.getCodsuscrip(),
                facturaResponse.getIdfactura(),
                "Pago de servicios",
                facturaResponse.getTotalfactura()
                );
        return sessionRequest;
    }

    ClientResponse save(SessionRequest sessionRequest, ClientResponse clientResponse, String id) {
        authRepository.save(new AuthModel(sessionRequest, clientResponse.getRequestId(), id));
        clientResponse.setActualizado("S");

        return clientResponse;
    }

    public ClientRequest createRequest(SessionRequest sessionRequest, String id) {
        String locale = "es_CO";
        String returnUrl = "https://serviciudad.gov.co/apppse/#/finalizar/" + id;
        String ipAddress = "127.0.0.1";
        String userAgent = "PlacetoPay Sandbox";

        return new ClientRequest(
                locale,
                utilService.createAuth(),
                createPayment(sessionRequest),
                utilService.getExpiration(),
                returnUrl,
                ipAddress,
                userAgent
        );
    }


    private Payment createPayment(SessionRequest sessionRequest) {
        return new Payment(
                sessionRequest.getReference(),
                sessionRequest.getDescripcion(),
                createAmount(sessionRequest.getTotal()),
                false
                );
    }

    private Amount createAmount(long total) {
        return new Amount("COP", total);
    }

    public List<AuthModel> listar() {
        return (List<AuthModel>) authRepository.findAll();
    }

    public List<AuthModel> listarpendientes() {
        return authRepository.findByEstado(Constantes.ESTADO_PENDIENTE);
    }

    public List<ValidaciomModel> listarConfirmadoNoRegistrado() {
        List<AuthModel> l =  authRepository.findByEstadoPagoConfirmado(Constantes.APPROVED, "S");
        List<ValidaciomModel> validaciomModels = new ArrayList<>();
        l.forEach(authModel -> {
            try {
                FacturaRequest facturaRequest = new FacturaRequest(authModel.getCuenta());

                String existe = facturaService.existePagoEnBaseRecaudo(authModel.getCuenta(), authModel.getReference());

                if (existe.equals("N")) {
                    authModel.setPagoconfirmado("X");
                    validaciomModels.add(new ValidaciomModel(
                                                authModel.getCuenta(),
                                                authModel.getReference(),
                                                authModel.getTotal(),
                                                authModel.getFecha(),
                                                authModel.getPagoconfirmado()
                    ));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return validaciomModels;
    }
}
