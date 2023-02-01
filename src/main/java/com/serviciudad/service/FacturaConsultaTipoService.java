package com.serviciudad.service;

import com.serviciudad.model.*;
import com.serviciudad.repository.AuthRepository;
import com.serviciudad.repository.CronRepository;
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

    public FacturaResponse consultarFacturaTipo(FacturaTipoRequest facturaTipoRequest) {
        FacturaResponse facturaResponse;
        WebClient webClient = WebClient.create(URL_RECAUDO);
        try {
            facturaResponse = webClient.post()
                    .uri("/rec/consultafacturatipo")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(Mono.just(facturaTipoRequest), FacturaRequest.class)
                    .retrieve()
                    .bodyToMono(FacturaResponse.class)
                    .timeout(Duration.ofSeconds(20))  // timeout
                    .block();
        } catch (Exception e) {
            errorService.save(e, "", "Consultando la factura");
            throw e;
        }

        if (facturaResponse.getFechapago() != null && facturaResponse.getFechapago().length() == 10) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Integer fechaPago = Integer.parseInt(facturaResponse.getFechapago().replace("-", ""));
            Integer fechaActual = Integer.parseInt(dateFormat.format(new Date()));

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
}
