package com.serviciudad.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class MyService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static Map<String, Long> valores = new HashMap<>();


    public synchronized boolean existeLlave(String key) {
        Long valor = valores.get(key);
        boolean existe = false;

        if (valor == null) {
            valores.put(key, generarFechaActual());
        } else {
            existe = true;
            for(Map.Entry<String, Long> entrada : valores.entrySet()) {
                if (((generarFechaActual() - entrada.getValue()) / 1000) > 5) {
                    remove(key);
                    existe = false;
                }
            }
        }
        return existe;
    }

    private long generarFechaActual() {
        Date fechaActual = new Date();
        return fechaActual.getTime();
    }

    private synchronized void remove(String key) {
        valores.remove(key);
    }
/*
    public String getValue(String key) {
        try {
            return redisTemplate.opsForValue().get(key).toString();
        } catch (RedisConnectionFailureException exception) {
            return null;
        }

    }
/*
    public void setValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public String getValue(String key) {
        try {
            return redisTemplate.opsForValue().get(key).toString();
        } catch (RedisConnectionFailureException exception) {
            return null;
        }

    }
    */

    /*
    public Object getValue(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (RedisConnectionFailureException exception) {
            return null;
        }
    }
    */
}

