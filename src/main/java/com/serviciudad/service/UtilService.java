package com.serviciudad.service;

import com.serviciudad.model.Auth;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Service
public final class UtilService {
    @Autowired
    private Environment env;


    public String getFecha(int incrementoMinutos) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, 5);
        calendar.add(Calendar.MINUTE, incrementoMinutos);

        SimpleDateFormat isoDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        return isoDate.format(calendar.getTime()) + "+00:00";
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

    public Auth createAuth()  {
        String login = env.getProperty("login");
        String secrete =  env.getProperty("secrete");
        String seed = getSeed();
        String nonce = bin2String(randomBytes());

        byte[] bytes = sha1(nonce + seed + secrete);

        String tranKey = Base64.getEncoder().encodeToString(bytes);
        String nonceBase64 = Base64.getEncoder().encodeToString(nonce.getBytes());

        return new Auth(login, tranKey, nonceBase64, seed);
    }

    public String getSeed() {
        return getFecha(3);
    }

    public String getExpiration() {
        return getFecha(20);
    }
}
