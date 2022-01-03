package com.serviciudad.service;


import com.serviciudad.model.ErrorModel;
import com.serviciudad.repository.AuthRepository;
import com.serviciudad.repository.ErrorRepository;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public final class ErrorService {
    @Autowired
    ErrorRepository errorRepository;

    public void save(Exception e) {
        errorRepository.save(new ErrorModel(e, "", ""));
    }

    public void save(Exception e, String datos, String origen) {
        errorRepository.save(new ErrorModel(e, datos, origen));
    }

    public List<ErrorModel> listar() {
        return (List<ErrorModel>) errorRepository.findAll();
    }

    public void borrar() {
        errorRepository.deleteAll();
    }
}
