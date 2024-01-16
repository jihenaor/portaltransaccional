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

    @Value("${url_sicesp}")
    private String URL_SICESP;

    public FacturaConsultaTipoService() {
    }

    public FacturaResponse consultarFacturaTipo(FacturaTipoRequest facturaTipoRequest) {
        FacturaResponse facturaResponse = null;
        WebClient webClient = WebClient.create(URL_SICESP);
        String URI = "/rec/consultafacturatipo";

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
}
