package com.serviciudad.controller.test;

import com.serviciudad.model.TestResponse;
import com.serviciudad.service.TestrecaudoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public final class TestrecaudoController {
    @Autowired
    private TestrecaudoService testrecaudoService;

    @RequestMapping(value = "/testrecaudo", method = RequestMethod.GET)
    public ResponseEntity<TestResponse> test() {
        try {
            TestResponse response = new TestResponse(testrecaudoService.testRecaudo("test"));

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
}