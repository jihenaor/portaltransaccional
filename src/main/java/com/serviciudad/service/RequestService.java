package com.serviciudad.service;

import com.serviciudad.compartido.exceptions.ApiUnauthorized;
import com.serviciudad.entity.*;
import com.serviciudad.model.ClientResponse;
import com.serviciudad.model.JwtResponse;
import com.serviciudad.model.SessionRequest;
import com.serviciudad.model.UserResponse;
import com.serviciudad.repository.RequestRepository;
import com.serviciudad.repository.UserRepository;
import com.serviciudad.security.JwtIO;
import com.serviciudad.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RequestService {

    @Autowired
    RequestRepository requestRepository;

    public void save(String codsuscrip,
                     String numerofactura,
                     String tipoFactura,
                     String codigoRespuesta,
                     String comentario) {
        requestRepository.save(new RequestModel(codsuscrip,
                numerofactura,
                tipoFactura,
                codigoRespuesta,
                comentario));
    }
}
