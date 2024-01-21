package com.serviciudad.service;

import com.serviciudad.model.FacturaRequest;
import com.serviciudad.models.factura.application.ports.FacturaResponse;
import com.serviciudad.model.FacturasResponse;
import com.serviciudad.repository.AuthRepository;
import com.serviciudad.repository.CronRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public final class FacturaConsultasService {
    @Autowired
    AuthRepository authRepository;

    @Autowired
    CronRepository cronRepository;

    @Autowired
    private UtilService utilService;

    @Autowired
    private Environment env;

    @Value("${url_recaudo}")
    private String URL_RECAUDO;

    public List<FacturaResponse> consultarFacturas(FacturaRequest facturaRequest) {
        FacturasResponse facturasResponse;
        facturasResponse = consultarFacturasPendientesPago(facturaRequest);

        facturasResponse.getRespuestafacturas().forEach(facturaResponse -> {

        });

        return facturasResponse.getRespuestafacturas();
    }

    private FacturasResponse consultarFacturasPendientesPago(FacturaRequest facturaRequest) {
        FacturasResponse facturasResponse = null;
        WebClient webClient = WebClient.create(URL_RECAUDO);
        int limite = 3;
        for (int cont = 0; cont < limite; cont++) {
            try {
                facturasResponse = webClient.post()
                        .uri("/rec/consultafacturas")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .body(Mono.just(facturaRequest), FacturaRequest.class)
                        .retrieve()
                        .bodyToMono(FacturasResponse.class)
                        .timeout(Duration.ofSeconds(20))  // timeout
                        .block();
                break;
            } catch (Exception e) {
                log.error("consultarFacturasPendientesPago: " + cont + " de " + limite, e);
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
        return facturasResponse;
    }
}
