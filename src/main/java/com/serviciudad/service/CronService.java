
package com.serviciudad.service;


import com.serviciudad.entity.AuthModel;
import com.serviciudad.entity.CronModel;
import com.serviciudad.exception.DomainExceptionCuentaNoExiste;
import com.serviciudad.model.*;
import com.serviciudad.repository.AuthRepository;
import com.serviciudad.repository.CronRepository;
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
public final class CronService {
    @Autowired
    CronRepository cronRepository;

    public List<CronModel> listar() {
        return (List<CronModel>) cronRepository.findAll();
    }
}
