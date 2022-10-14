package com.serviciudad.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serviciudad.constantes.Constantes;
import com.serviciudad.entity.AuthModel;
import com.serviciudad.entity.ValidaciomModel;
import com.serviciudad.exception.DomainExceptionCuentaNoExiste;
import com.serviciudad.exception.DomainExceptionPlaceToPay;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public final class ListarAprobadosNoConfirmadosService {
    @Autowired
    AuthRepository authRepository;

    @Autowired
    private FacturaEvertecService facturaEvertecService;

    @Autowired
    private ConsultaExisteRecaudoSicepService consultaExisteRecaudoSicepService;

    public List<AuthModel> listarAprobadosNoConfirmados() {
        List<AuthModel> l =  authRepository.findByEstadoPagoConfirmado(Constantes.APPROVED, "N", "%");
        List<AuthModel> authModels = new ArrayList<>();
        l.forEach(authModel -> {
            try {
                String existeSicep = consultaExisteRecaudoSicepService.existePagoEnBaseRecaudo(authModel.getCuenta(), authModel.getReference());
                String existeTemporal = facturaEvertecService.existePagoEnBaseRecaudo(authModel.getCuenta(), authModel.getReference());

                if (existeSicep.equals("N") && existeTemporal.equals("N")) {
                    authModels.add(authModel);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return authModels;
    }
}
