package com.serviciudad.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyServiceTest {

    @Autowired
    MyService myService1;

    @Autowired
    MyService myService2;

    @Test
    public void existeLlaveTest() {
        String key = "key1";
        boolean resultado = myService1.existeLlave(key);
        assertFalse(resultado);

        resultado = myService2.existeLlave(key);
        assertTrue(resultado);
    }

    @Test
    public void existeLlaveLuegoDeSeisSegundosTest() {
        String key = "key2";
        boolean resultado = myService1.existeLlave(key);
        assertFalse(resultado);

        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        resultado = myService2.existeLlave(key);
        assertFalse(resultado);
    }
}