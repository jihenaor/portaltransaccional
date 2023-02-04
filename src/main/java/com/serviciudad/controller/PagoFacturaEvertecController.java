package com.serviciudad.controller;

import com.serviciudad.entity.IdRecaudoModel;
import com.serviciudad.model.PagoEvertecRequest;
import com.serviciudad.modelpago.RespuestaResponse;
import com.serviciudad.service.ErrorService;
import com.serviciudad.service.FacturaEvertecService;
import com.serviciudad.service.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public final class PagoFacturaEvertecController {
    @Autowired
    private FacturaEvertecService facturaService;

    @Autowired
    private ErrorService errorService;

    @Autowired
    private MyService myService;

    private final String KEY = "EVERTECID-";

    @RequestMapping(value = "/pagarfactura", method = RequestMethod.POST)
    public ResponseEntity<RespuestaResponse> pagarfactura(@RequestBody PagoEvertecRequest pagoRequest) {

        String idEnProceso = myService.getValue(getKey(pagoRequest.getId()));

        if (idEnProceso == null) {
            myService.setValue(getKey(pagoRequest.getId()), pagoRequest.getId());
        } else {
            errorService.save(new Exception("El id proceso ya esta en curso " + pagoRequest.getId()));
            return ResponseEntity.internalServerError().build();
        }

        try {
            RespuestaResponse respuestaResponse = facturaService.pagarFactura(Optional.of(new IdRecaudoModel(pagoRequest.getId())),
                    false,
                    Optional.empty());
            myService.remove(getKey(pagoRequest.getId()));
            return ResponseEntity.ok().body(respuestaResponse);

        } catch (Exception e) {
            errorService.save(e);
            myService.remove(getKey(pagoRequest.getId()));
            return ResponseEntity.internalServerError().body(null);
        }
    }

    private String getKey(String id) {
        return KEY.concat(id);
    }
}