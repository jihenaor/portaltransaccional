package com.serviciudad.service;


import com.serviciudad.model.*;
import com.serviciudad.repository.AuthRepository;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
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
public final class AuthService {
    @Autowired
    AuthRepository authRepository;

    @Autowired
    private FacturaService facturaService;

    @Autowired
    private ErrorService errorService;

    public ClientResponse auth(FacturaRequest facturaRequest) {
        ClientResponse clientResponse;
        SessionRequest sessionRequest;
        WebClient webClient;
        FacturaResponse facturaResponse;

        try {
            webClient = WebClient.create("https://checkout-test.placetopay.com/api");
        } catch (Exception e) {
            errorService.save(e);
            throw e;
        }

        sessionRequest = getSessionRequest(facturaRequest);

        ClientRequest clientRequest = createRequest(sessionRequest);

        sessionRequest.setLogin(clientRequest.getAuth().getLogin());
        sessionRequest.setNonce(clientRequest.getAuth().getNonce());
        sessionRequest.setTrankey(clientRequest.getAuth().getTranKey());
        sessionRequest.setSeed(clientRequest.getAuth().getSeed());

        try {
            clientResponse = webClient.post()
                    .uri("/session")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(Mono.just(clientRequest), ClientRequest.class)
                    .retrieve()

                    .bodyToMono(ClientResponse.class)
                    .timeout(Duration.ofSeconds(20))  // timeout
                    .block();
        } catch (Exception e) {
            errorService.save(e);
            throw e;
        }

        save(sessionRequest, clientResponse.getRequestId());

        return clientResponse;
    }

    private SessionRequest getSessionRequest(FacturaRequest facturaRequest) {
        FacturaResponse facturaResponse;
        SessionRequest sessionRequest;
        try {
            facturaResponse = facturaService.consultaFactura(facturaRequest);
        } catch (Exception e) {
            errorService.save(e);
            throw e;
        }

        sessionRequest = new SessionRequest(
                facturaRequest.getCodsuscrip(),
                facturaResponse.getIdfactura(),
                facturaResponse.getDescripcion() + " Pago de servicios",
                facturaResponse.getTotalfactura()
                );
        return sessionRequest;
    }

    private void save(SessionRequest sessionRequest, int requestId) {
        authRepository.save(
                new AuthModel(sessionRequest, requestId));
    }

    public ClientRequest createRequest(SessionRequest sessionRequest) {
        String locale = "es_CO";
        String returnUrl = "https://serviciudad.gov.co/apppse/#/finalizar/"
                + sessionRequest.getCuenta()
                + "/" + sessionRequest.getReference();
        String ipAddress = "127.0.0.1";
        String userAgent = "PlacetoPay Sandbox";

        return new ClientRequest(
                locale,
                createAuth(),
                createPayment(sessionRequest),
                getExpiration(),
                returnUrl,
                ipAddress,
                userAgent
        );
    }

    private String getFecha(int incrementoMinutos) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, 5);
        calendar.add(Calendar.MINUTE, incrementoMinutos);

        SimpleDateFormat isoDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        return isoDate.format(calendar.getTime()) + "+00:00";
    }

    private String getSeed() {
        return getFecha(3);
    }

    private String getExpiration() {
        return getFecha(20);
    }

    public byte[] sha1(String toHash) {
        byte[] bytes = null;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            bytes = toHash.getBytes(StandardCharsets.US_ASCII); //I tried UTF-8, ISO-8859-1...
            digest.update(bytes, 0, bytes.length);
            bytes = digest.digest();
        }
        catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    private String bin2String(byte[] hex){
        return HexUtils.toHexString(hex);
    }

    private byte[] randomBytes() {
        Random random = ThreadLocalRandom.current();
        byte[] r = new byte[16];
        random.nextBytes(r);
        return r;
    }

    private Auth createAuth()  {
        String login = "30d07df739dc0667bbdd7c89fc654597";
        String secrete = "juL6nhF30Ve5GX4G";
        String seed = getSeed();
        String nonce = bin2String(randomBytes());

        byte[] bytes = sha1(nonce + seed + secrete);

        String tranKey = Base64.getEncoder().encodeToString(bytes);
        String nonceBase64 = Base64.getEncoder().encodeToString(nonce.getBytes());

        return new Auth(login, tranKey, nonceBase64, seed);
    }

    private Payment createPayment(SessionRequest sessionRequest) {
        return new Payment(
                sessionRequest.getReference(),
                sessionRequest.getDescripcion(),
                createAmount(sessionRequest.getTotal()),
                false
                );
    }

    private Amount createAmount(long total) {
        return new Amount("COP", total);
    }

    public List<AuthModel> listar() {
        return (List<AuthModel>) authRepository.findAll();
    }
}
