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

import java.util.List;
import java.util.Optional;

@Service
public class RequestService {

    @Autowired
    RequestRepository requestRepository;

    public void save(String codsuscrip,
                     String numerofactura,
                     String tipoFactura,
                     String banco,
                     String codigoRespuesta,
                     String comentario,
                     Long total) {
        requestRepository.save(new RequestModel(codsuscrip,
                numerofactura,
                tipoFactura,
                banco,
                codigoRespuesta,
                comentario,
                total));
    }

    public List<RequestModel> findByCodsuscrip(String codsuscrip) {
        return requestRepository.findByCodsuscrip(codsuscrip);
    }

    public List<RequestModel> findAll() {
        return (List<RequestModel>) requestRepository.findAll();
    }

}
