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
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.UUID;


//Tarjeta de prueba 4005580000000040
//

@Service
public final class FacturaService {
    @Autowired
    AuthRepository authRepository;

    @Autowired
    CronRepository cronRepository;

    @Autowired
    private ErrorService errorService;

    @Autowired
    private UtilService utilService;

    @Autowired
    private Environment env;

    private final String URL_RECAUDO = "http://192.168.100.72:8080/recaudos/api";

    public FacturaResponse consultaFactura(FacturaRequest facturaRequest) throws DomainExceptionCuentaNoExiste {
        FacturaResponse facturaResponse;
        AuthModel authModel;
        PagoResponse pagoResponse;

        facturaResponse = consultarFactura(facturaRequest);

        if (facturaResponse.getCodRespuesta() == 1) {
            throw new DomainExceptionCuentaNoExiste();
        }

        try {
            authModel = authRepository.findByCuentaAndReferenceEstado(
                                                facturaResponse.getCuenta(),
                                                facturaResponse.getIdfactura(),
                                                Constantes.APPROVED);
        } catch (Exception e) {
            errorService.save(e, "", String.format("Error consultando cuenta por referencia: %s, %s",
                    facturaResponse.getCuenta(),
                    facturaResponse.getIdfactura()));
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

    private PagoFacturaResponse enviarPago(AuthModel authModel) {
        PagoFacturaResponse pagoFacturaResponse;
        WebClient webClient = WebClient.create(URL_RECAUDO);

        String codigoBanco =  env.getProperty("CODIGOBANCOPLACETOPAY");

        PagoFacturaRequest pagoFacturaRequest = new PagoFacturaRequest(
                authModel.getCuenta(),
                authModel.getReference(),
                codigoBanco,
                authModel.getRequestid() + ""
        );

        try {
            pagoFacturaResponse = webClient.post()
                    .uri("/rec/pagarfactura")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(Mono.just(pagoFacturaRequest), FacturaRequest.class)
                    .retrieve()
                    .bodyToMono(PagoFacturaResponse.class)
                    .timeout(Duration.ofSeconds(20))  // timeout
                    .block();
            authModel.setPagoconfirmado("S");
            update(authModel);
        } catch (Exception e) {
            errorService.save(e, "", "Registrando pago de factura");
            throw e;
        }
        return pagoFacturaResponse;
    }

    public FacturaResponse consultarFactura(FacturaRequest facturaRequest) {
        FacturaResponse facturaResponse;
        WebClient webClient = WebClient.create(URL_RECAUDO);
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
        return facturaResponse;
    }

    public PagoResponse consultarEstadoPago(AuthModel authModel) {
        PagoResponse pagoResponse;
        WebClient webClient;
        String json = "";

        try {
            webClient = WebClient.create(env.getProperty("url"));
        } catch (Exception e) {
            errorService.save(e);
            throw e;
        }
        AuthRequestInformation authRequestInformation = new AuthRequestInformation(
                utilService.createAuth());

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

    public RespuestaResponse pagarFactura(PagoRequest pagoRequest, boolean porCron) {
        PagoResponse pagoResponse;
        RespuestaResponse respuestaResponse;
        AuthModel authModel = consulta(pagoRequest);

        if (authModel == null) {

        }

        try {
            pagoResponse = consultarEstadoPago(authModel);

            if (!authModel.getEstado().equals(pagoResponse.getStatus().getStatus())) {
                authModel.setEstado(pagoResponse.getStatus().getStatus());
                if (pagoResponse.getPayment() != null && pagoResponse.getPayment().get(0) != null) {
                    authModel.setAutorizacion(pagoResponse.getPayment().get(0).getAuthorization());
                }

                update(authModel);
                if (authModel.getEstado().trim().equals(Constantes.APPROVED)) {
                    enviarPago(authModel);
                }
            } else {
                if (porCron) {
                    authModel.setFechaultimointento((new Date()).toString());
                }
            }
        } catch (Exception e) {
            if (e instanceof WebClientResponseException.Unauthorized) {
                authModel.setEstado("UNAUTHORIZED");
                authModel.setFechaultimointento((new Date()).toString());
                update(authModel);
            }
            throw e;
        }

        String status;

        switch (pagoResponse.getStatus().getStatus()) {
            case "APPROVED":
                status = "APROBADO";
                break;
            case "REJECTED":
                status = "RECHAZADO";
                break;
            default:
                status = pagoResponse.getStatus().getStatus();
        }

        respuestaResponse = new RespuestaResponse(pagoResponse.getStatus().getDate(),
                pagoResponse.getRequest().getPayment().getAmount().currency,
                String.valueOf(pagoResponse.getRequest().getPayment().getAmount().total),
                pagoResponse.getPayment() == null ? authModel.getReference() : pagoResponse.getPayment().get(0).getReference(),
                status,
                pagoResponse.getStatus().getMessage(),
                authModel.getAutorizacion(),
                authModel.getCuenta());


        return respuestaResponse;
    }

    public void enviarPagoAutorizadoPorCron(PagoRequest pagoRequest) {
        AuthModel authModel = consulta(pagoRequest);

        if (authModel != null) {
            enviarPago(authModel);
        }
    }

    private void update(AuthModel authModel) {
        authRepository.save(authModel);
    }

    public AuthModel consulta(PagoRequest pagoRequest) {
        return authRepository.findByiD(pagoRequest.getId());
    }

    public List<AuthModel> consultaCuentaFactura(String cuenta, String factura) {
        return authRepository.findByCuentaAndReference(cuenta, factura);
    }
    public List<AuthModel> consultaFactura(String factura) {
        return authRepository.findByReference(factura);
    }

    public AuthModel consultaByRequestidAndReference(NotificacionRequest notificacionRequest) {
        return authRepository.findByRequestidAndReference(notificacionRequest.getRequestID(), notificacionRequest.getReference());
    }

    public void seleccionarPagosPendientes() {
        List<AuthModel> authModels = authRepository.findByEstado(Constantes.ESTADO_PENDIENTE);

        cronRepository.save(new CronModel(UUID.randomUUID().toString(), (new Date()).toString(), authModels.size()));

        authModels.forEach(authModel -> {
            PagoRequest pagoRequest = new PagoRequest(authModel.getId());
            try {
                pagarFactura(pagoRequest, true);
            } catch (Exception e) {

            }
        });
    }

    public void seleccionarPagosAprobadosSinRegistrar() {
        List<AuthModel> authModels = authRepository.findByEstadoPagoConfirmado(Constantes.APPROVED, "N");

        authModels.forEach(authModel -> {
            PagoRequest pagoRequest = new PagoRequest(authModel.getId());
            try {
                enviarPagoAutorizadoPorCron(pagoRequest);
            } catch (Exception e) {

            }
        });
    }

    public void seleccionarPagosAprobadosConfirmadosValidar() {
        List<AuthModel> authModels = authRepository.findByEstadoPagoConfirmado(Constantes.APPROVED, "S");

        authModels.forEach(authModel -> {
            PagoRequest pagoRequest = new PagoRequest(authModel.getId());
            String existe = existePagoEnBaseRecaudo(authModel.getCuenta(), authModel.getReference());
            if (existe.equals("N")) {
                try {
                    System.out.println("procesando: " + authModel.getCuenta());
                    enviarPagoAutorizadoPorCron(pagoRequest);
                } catch (Exception e) {

                }
            } else {
                System.out.println("Existe: " + authModel.getCuenta());
            }
        });
    }

    public void notificarTransaccion(NotificacionRequest notificacionRequest) throws DomainExceptionNoEncontradoRequestId {
        PagoResponse pagoResponse;
        AuthModel authModel = consultaByRequestidAndReference(notificacionRequest);

        if (authModel == null) {
            throw new DomainExceptionNoEncontradoRequestId();
        }

        if (!authModel.getEstado().equals(Constantes.ESTADO_PENDIENTE)) {
            return;
        }

        try {
            pagoResponse = consultarEstadoPago(authModel);

            if (!authModel.getEstado().equals(pagoResponse.getStatus().getStatus())) {
                authModel.setEstado(pagoResponse.getStatus().getStatus());
                authModel.setAutorizacion(pagoResponse.getRequest().getPayment().getAuthorization());
                update(authModel);
            }
        } catch (Exception e) {
            if (e instanceof WebClientResponseException.Unauthorized) {
                authModel.setEstado(Constantes.UNAUTHORIZED);
                authModel.setFechaultimointento((new Date()).toString());
                update(authModel);
            }
            throw e;
        }
    }

    public String existePagoEnBaseRecaudo(String cuenta, String factura) {
        FacturaResponse facturaResponse;
        WebClient webClient = WebClient.create(URL_RECAUDO);
        String existeFactura;

        try {
            existeFactura = webClient.get()
                    .uri("/rec/consulta/" + cuenta +"/" + factura)
                    .exchange()
                    .block()
                    .bodyToMono(String.class)
                    .block();

        } catch (Exception e) {
            errorService.save(e, "", "Consultando existe factura");
            throw e;
        }
        return existeFactura;
    }

    public List<AuthModel> validarFactura(String factura) {
        RespuestaResponse respuestaResponse;
        List<AuthModel> authModel = consultaFactura(factura);

        authModel.forEach(authModel1 -> {
            PagoResponse pagoResponse;

            try {
                pagoResponse = consultarEstadoPago(authModel1);

                String status;

                switch (pagoResponse.getStatus().getStatus()) {
                    case "APPROVED":
                        status = "APROBADO";
                        break;
                    case "REJECTED":
                        status = "RECHAZADO";
                        break;
                    default:
                        status = pagoResponse.getStatus().getStatus();
                }

                authModel1.setEstadoevertec(status);
            } catch (Exception e) {

                throw e;
            }
        });


        return authModel;
    }
}
