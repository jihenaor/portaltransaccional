package com.serviciudad.service;

import com.serviciudad.compartido.exceptions.BusinessException;
import com.serviciudad.model.*;
import com.serviciudad.repository.AuthRepository;
import com.serviciudad.repository.CronRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

@Service
public final class FacturaConsultaTipoService {

    private static final Logger logger = LoggerFactory.getLogger(FacturaConsultaTipoService.class);

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

    @Value("${url_sicesp}")
    private String URL_SICESP;

    public FacturaConsultaTipoService() {
    }

    public FacturaResponse consultarFacturaTipo(FacturaTipoRequest facturaTipoRequest) throws BusinessException {
        FacturaResponse facturaResponse = null;
        WebClient webClient = WebClient.create(URL_SICESP);
        String URI = "/rec/consultafacturatipo";

        validarDatos(facturaTipoRequest);

//        int limite = 4;
//        for (int cont = 0; cont < limite; cont++) {
//            System.out.println("Consultando: " + cont);
            try {
                facturaResponse = webClient.post()
                        .uri(URI)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .body(Mono.just(facturaTipoRequest), FacturaRequest.class)
                        .retrieve()
                        .bodyToMono(FacturaResponse.class)
                        .timeout(Duration.ofSeconds(20))  // timeout
                        .block();
//                break;
            } catch (Exception e) {
                System.out.println("Error consultando: "  + URL_SICESP+URI);
                e.printStackTrace();
//                if (cont + 1 == limite) {
//                    errorService.save(e, "", "consultarFacturaTipo. Cont: " + (cont + 1) +".  Cuenta: " + facturaTipoRequest.getCodsuscrip());

                    throw e;
  /*              } else {
                    try {
                        TimeUnit.SECONDS.sleep(3);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }

   */
            }
//        }

        assert facturaResponse != null;
        if (facturaResponse.getFechapago() != null && facturaResponse.getFechapago().length() == 10) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            int fechaPago = Integer.parseInt(facturaResponse.getFechapago().replace("-", ""));
            int fechaActual = Integer.parseInt(dateFormat.format(new Date()));

            if (facturaTipoRequest.getTipoFactura().equals("0")
                    || facturaTipoRequest.getTipoFactura().equals("00")) {
                facturaResponse.setFacturavencida(fechaActual > fechaPago ? "S" : "N");
            } else {
                facturaResponse.setFacturavencida("N");
            }
        } else {
            facturaResponse.setFacturavencida("N");
        }

        facturaResponse.setTipofact(Integer.parseInt(facturaTipoRequest.getTipoFactura()));

        return facturaResponse;
    }

    private void validarDatos(FacturaTipoRequest facturaTipoRequest) throws BusinessException {
        String mensajeBussinesError = "Los parametros de seleccion son incorrectos e.";
        String log = facturaTipoRequest.toString() + ". ";

        if (facturaTipoRequest.getCodsuscrip() == null || facturaTipoRequest.getCodsuscrip().isEmpty()) {
            log += "Código de suscriptor nulo o vacío";
            logger.error(log);

            throw new BusinessException(mensajeBussinesError + log);
        }

        if (facturaTipoRequest.getTipoFactura() == null) {
            logger.error("El tipo de factura no puede ser nulo."  + facturaTipoRequest.toString());
            throw new BusinessException(mensajeBussinesError);
        }

        switch (facturaTipoRequest.getTipoFactura()) {
            case "0":
                break;
            case "1":
            case "6":
                if (facturaTipoRequest.getValor() == null || facturaTipoRequest.getValor() == 0) {
                    log += "El valor no puede ser nulo o cero para el tipo de factura "  + facturaTipoRequest.getTipoFactura();
                    logger.error(log);
                }
                if (facturaTipoRequest.getNumerofactura() == null || facturaTipoRequest.getNumerofactura().isEmpty()) {
                    log += "El número de factura no puede ser nulo o vacío para el tipo de factura " + facturaTipoRequest.getTipoFactura();
                    logger.error(log + "." + facturaTipoRequest.toString());
                }

                throw new BusinessException(mensajeBussinesError + log);
            default:
                if (facturaTipoRequest.getNumerofactura() == null || facturaTipoRequest.getNumerofactura().isEmpty()) {
                    log += "El número de factura no puede ser nulo o vacío para el tipo de factura " + facturaTipoRequest.getTipoFactura() + ".";
                    logger.error(log);
                    throw new BusinessException(mensajeBussinesError + log);
                }
        }
    }
}
