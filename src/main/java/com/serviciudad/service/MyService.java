package com.serviciudad.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MyService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static Map<String, String> valores = new HashMap<>();

    public synchronized void setValue(String key, String value) {
        valores.put(key, value);
    }

    public synchronized String getValue(String key) {
        return valores.get(key);
    }

    public synchronized void remove(String key) {
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

