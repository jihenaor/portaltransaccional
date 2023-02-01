package com.serviciudad.controller.resquest;

import com.serviciudad.entity.AuthModel;
import com.serviciudad.entity.RequestModel;
import com.serviciudad.service.FacturaEvertecService;
import com.serviciudad.service.RequestService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins  = "*" )
@RequestMapping("/api/request")
public final class RequestController {
    @Autowired
    private RequestService requestService;


    @RequestMapping(value = "/{codsuscrip}", method = RequestMethod.GET)
    @SecurityRequirement(name = "Bearer Authentication")
    public List<RequestModel> consultar(@PathVariable ("codsuscrip") String codsuscrip) {
            return requestService.findByCodsuscrip(codsuscrip);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @SecurityRequirement(name = "Bearer Authentication")
    public List<RequestModel> consultarAll() {
        return requestService.findAll();
    }
}