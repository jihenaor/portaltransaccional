package com.serviciudad.service;

import com.serviciudad.exception.DomainExceptionCuentaNoExiste;
import com.serviciudad.model.FacturaRequest;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class FacturaEvertecServiceTest {
    @Autowired
    FacturaEvertecService facturaService;

    @Test
    void doxx() throws DomainExceptionCuentaNoExiste {

        String result = facturaService.retryExample("error");
        Assert.assertEquals("Retry Recovery OK!", result);

    }
}
