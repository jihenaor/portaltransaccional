package com.serviciudad.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serviciudad.constantes.Constantes;
import com.serviciudad.entity.AuthModel;
import com.serviciudad.entity.CuentaModel;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Service
public final class AuthRecaudoService {
    @Autowired
    AuthRepository authRepository;

    @Autowired
    private FacturaEvertecService facturaService;

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

        if (existeSessionEnCurso(new CuentaModel(facturaRequest.getCodsuscrip()))) {
            return new ClientResponse();
        }

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

    public List<AuthModel> listarpendientes() {
        return authRepository.findByEstado(Constantes.ESTADO_PENDIENTE);
    }

    private boolean existeSessionEnCurso(CuentaModel cuentaModel) {
        List<AuthModel> authModels;
        AtomicReference<Boolean> existe = new AtomicReference<>(false);

        try {
            authModels = authRepository.findByCuentaEstado(cuentaModel.getValue(), Constantes.ESTADO_PENDIENTE);

            authModels.forEach(authModel -> {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date fechaRecaudo = null;

                try {
                    fechaRecaudo = dateFormat.parse(authModel.getFecha());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                long diff = (new Date()).getTime() - fechaRecaudo.getTime();
                TimeUnit time = TimeUnit.MINUTES;
                long diffrence = time.convert(diff, TimeUnit.MILLISECONDS);

                if (diffrence < 10) {
                    existe.set(true);
                }
            });
        } catch (Exception e) {
            throw e;
        }
        return existe.get();
    }
}
