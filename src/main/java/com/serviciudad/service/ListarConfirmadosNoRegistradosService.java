package com.serviciudad.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serviciudad.compartido.model.ValueObjectDomain;
import com.serviciudad.compartido.model.ValueStringDomain;
import com.serviciudad.constantes.Constantes;
import com.serviciudad.entity.AuthModel;
import com.serviciudad.entity.CuentaModel;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Service
public final class ListarConfirmadosNoRegistradosService {
    @Autowired
    AuthRepository authRepository;

    @Autowired
    private FacturaEvertecService facturaService;

    public List<ValidaciomModel> listarConfirmadoNoRegistrado(ValueStringDomain fecha) {
        List<AuthModel> l =  authRepository.findByEstadoPagoConfirmadoFecha(Constantes.APPROVED, "S", fecha.getValue().concat("%"));
        List<ValidaciomModel> validaciomModels = new ArrayList<>();
        l.forEach(authModel -> {
            try {
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
