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
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

@Service
public final class FacturaEvertecService {
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

    @Value("${url_recaudo}")
    private String URL_RECAUDO;

    static final Logger LOGGER = Logger.getLogger(String.valueOf(ActualizarDiarioController.class));

    public FacturaEvertecService() {
    }

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

    private PagoFacturaResponse enviarPagoEvertec(AuthModel authModel, Boolean validaSinRegistrar, Boolean validarEstadoCuenta) {
        PagoFacturaResponse pagoFacturaResponse;
        WebClient webClient = WebClient.create(URL_RECAUDO);

        String codigoBanco =  env.getProperty("CODIGOBANCOPLACETOPAY");
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());

        PagoFacturaEvertecRequest pagoFacturaRequest = new PagoFacturaEvertecRequest(
                authModel.getCuenta(),
                authModel.getReference(),
                codigoBanco,
                authModel.getRequestid() + "",
                authModel.getTotal(),
                authModel.getFecha(),
                validarEstadoCuenta ? "S" : "N"
        );

        try {
            String uriPagoFactura = validaSinRegistrar ? "/rec/pagarfacturasinregistrar" : "/rec/pagarfactura";

            pagoFacturaResponse = webClient.post()
                    .uri(uriPagoFactura)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(Mono.just(pagoFacturaRequest), FacturaRequest.class)
                    .retrieve()
                    .bodyToMono(PagoFacturaResponse.class)
                    .timeout(Duration.ofSeconds(20))  // timeout
                    .block();
            if (pagoFacturaResponse.getCodigoRespuesta().equals("1")) {
                authModel.setPagoconfirmado("S");
                authModel.setFechapago(date);
                update(authModel);
            } else if (pagoFacturaResponse.getCodigoRespuesta().equals("4")) {
                authModel.setError(pagoFacturaResponse.getComentario());
                update(authModel);
            } else if (pagoFacturaResponse.getCodigoRespuesta().equals("7")) {
                authModel.setError(pagoFacturaResponse.getComentario());
                authModel.setPagoconfirmado("S");
                authModel.setFechapago(authModel.getFecha());
                if (validaSinRegistrar) {
                    authModel.setError("Actualizado por cronJob");
                }
                update(authModel);
            } else if (pagoFacturaResponse.getComentario().indexOf("ya ha sido registrada") > 0) {
                authModel.setPagoconfirmado("S");
                authModel.setFechapago(date);
                update(authModel);
            } else {
                authModel.setError(pagoFacturaResponse.getComentario());
                update(authModel);
            }
        } catch (Exception e) {
            errorService.save(e, "", "Registrando pago de factura");
            authModel.setError("Registrando pago de factura" + " " + e.getMessage());

            update(authModel);
            throw e;
        }
        return pagoFacturaResponse;
    }

    public FacturaResponse consultarFactura(FacturaRequest facturaRequest) {
        FacturaResponse facturaResponse = null;
        WebClient webClient = WebClient.create(URL_RECAUDO);
        int limite = 3;

        for (int cont = 0; cont < limite; cont++) {
            try {
                facturaResponse = webClient.post()
                        .uri("/rec")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .body(Mono.just(facturaRequest), FacturaRequest.class)
                        .retrieve()
                        .bodyToMono(FacturaResponse.class)
                        .timeout(Duration.ofSeconds(20))  // timeout
                        .block();
                break;
            } catch (Exception e) {
                errorService.save(e, "", "consultarFactura " + cont + " de " + limite);
                if (cont + 1 == limite) {
                    throw e;
                } else {
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }

        if (facturaResponse.getFechapago() != null && facturaResponse.getFechapago().length() == 10){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Integer fechaPago = Integer.parseInt(facturaResponse.getFechapago().replace("-", ""));
            Integer fechaActual = Integer.parseInt(dateFormat.format(new Date()));

            facturaResponse.setFacturavencida(fechaActual > fechaPago ? "S" : "N");
        } else {
            facturaResponse.setFacturavencida("N");
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

    public RespuestaResponse pagarFactura(Optional<IdRecaudoModel> idRecaudoModel,
                                          boolean porCron,
                                          Optional<AuthModel> authModelPar) {
        PagoResponse pagoResponse;
        RespuestaResponse respuestaResponse;

        AuthModel authModel;

        if (authModelPar.isPresent()) {
            authModel = authModelPar.get();
        } else {
            authModel = consulta(idRecaudoModel.get());
        }

        if (authModel == null) {
            LOGGER.info(String.format("Error: %s %s",
                    idRecaudoModel.isPresent() ? "Pagorequest: -" + idRecaudoModel.get().getValue() : "-",
                    authModelPar.isPresent() ? "AuthModel: -" + authModelPar.get().getId() : "-")
            );
        }

        PagoFacturaResponse pagoFacturaResponse = null;

        try {
            pagoResponse = consultarEstadoPago(authModel);

            if (!authModel.getEstado().equals(pagoResponse.getStatus().getStatus())) {
                authModel.setEstado(pagoResponse.getStatus().getStatus());
                if (pagoResponse.getPayment() != null && pagoResponse.getPayment().get(0) != null) {
                    authModel.setAutorizacion(pagoResponse.getPayment().get(0).getAuthorization());
                }

                update(authModel);
                if (authModel.getEstado().trim().equals(Constantes.APPROVED)) {
                    pagoFacturaResponse = enviarPagoEvertec(authModel, false, false);
                }
            } else {
                if (porCron) {
                    authModel.setFechaultimointento((new Date()).toString());
                    authModel.setError("Estado evertec " + pagoResponse.getStatus().getStatus());
                    update(authModel);
                }
            }
        } catch (Exception e) {
            if (e instanceof WebClientResponseException.Unauthorized) {
                authModel.setEstado("UNAUTHORIZED");
                authModel.setFechaultimointento((new Date()).toString());
            } else {
                authModel.setError(e.getMessage());
            }
            update(authModel);
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
                authModel.getCuenta(),
                pagoFacturaResponse == null ? "N" : pagoFacturaResponse.getCodigoRespuesta().equals("1") ? "S" : "N");

        return respuestaResponse;
    }

    public PagoFacturaResponse enviarPagoAutorizadoPorCron(PagoEvertecRequest pagoRequest) {
        AuthModel authModel = consulta(new IdRecaudoModel(pagoRequest.getId()));
        PagoFacturaResponse pagoFacturaResponse = null;
        if (authModel != null) {
            pagoFacturaResponse = enviarPagoEvertec(authModel, true, false);
        }

        return pagoFacturaResponse;
    }

    public PagoFacturaResponse enviarPagoAutorizadoPorCron(AuthModel authModel) {
        PagoFacturaResponse pagoFacturaResponse = enviarPagoEvertec(authModel, true, true);

        return pagoFacturaResponse;
    }


    private void update(AuthModel authModel) {
        authRepository.save(authModel);
    }

    public AuthModel consulta(IdRecaudoModel idRecaudoModel) {
        return authRepository.findByiD(idRecaudoModel.getValue());
    }


    public List<AuthModel> consultaFactura(String factura) {
        return authRepository.findByReference(factura);
    }

    public AuthModel consultaByRequestidAndReference(NotificacionRequest notificacionRequest) {
        return authRepository.findByRequestidAndReference(notificacionRequest.getRequestId(), notificacionRequest.getReference());
    }

    public int seleccionarPagosPendientes() {
        List<AuthModel> authModels = authRepository.findByEstado(Constantes.ESTADO_PENDIENTE);
        AtomicInteger cont = new AtomicInteger();
        cronRepository.save(new CronModel(UUID.randomUUID().toString(), (new Date()).toString(), authModels.size()));

        authModels.forEach(authModel -> {
            try {
                RespuestaResponse respuestaResponse = pagarFactura(Optional.empty(), true, Optional.of(authModel));
                if (respuestaResponse.getPagoregistrado().equals("S")) {
                    cont.getAndIncrement();
                }
            } catch (Exception e) {
                errorService.save(e);
            }
        });
        cronRepository.save(new CronModel(UUID.randomUUID().toString(), (new Date()).toString(), cont.get()));
        return cont.get();
    }

    public void seleccionarPagosAprobadosSinRegistrar(ValueStringDomain fecha) {
        List<AuthModel> authModels = authRepository.findByEstadoPagoConfirmado(
                Constantes.APPROVED, "N", fecha.getValue().concat("%"));

        authModels.forEach(authModel -> {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date fechaRecaudo = dateFormat.parse(authModel.getFecha());

                long diff = (new Date()).getTime() - fechaRecaudo.getTime();
                TimeUnit time = TimeUnit.HOURS;
                long diffrence = time.convert(diff, TimeUnit.MILLISECONDS);

                if (diffrence > 3) {
                    PagoFacturaResponse pagoFacturaResponse = enviarPagoAutorizadoPorCron(authModel);

                    if (pagoFacturaResponse != null && pagoFacturaResponse.getCodigoRespuesta().equals("1")) {

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public String seleccionarPagosAprobadosConfirmadosValidar(ValueStringDomain fecha, String codigoBanco) {
        List<AuthModel> authModels = authRepository.findByEstadoPagoConfirmado(
                                            Constantes.APPROVED,
                                "S",
                                            fecha.getValue().concat("%"));
        AtomicInteger cont = new AtomicInteger();

        authModels.forEach(authModel -> {
            PagoEvertecRequest pagoRequest = new PagoEvertecRequest(authModel.getId());
            String existe = existePagoEnBaseRecaudo(authModel.getCuenta(), authModel.getReference(), codigoBanco);
            if (existe.equals("N")) {
                try {
                    cont.getAndIncrement();
                    enviarPagoAutorizadoPorCron(pagoRequest);
                } catch (Exception e) {

                }
            }
        });
        return "Aprobado evertec no existe recaudo: " + cont.get();
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

        /*

         */

        try {
            pagoResponse = consultarEstadoPago(authModel);

            if (!authModel.getEstado().equals(pagoResponse.getStatus().getStatus())) {
                authModel.setEstado(pagoResponse.getStatus().getStatus());
                authModel.setAutorizacion(pagoResponse.getRequest().getPayment().getAuthorization());

                if (authModel.getEstado().trim().equals(Constantes.APPROVED)) {
                   enviarPagoEvertec(authModel, false, false);
                } else {
                    update(authModel);
                }
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


    public List<AuthModel> validarFactura(String factura) {
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
