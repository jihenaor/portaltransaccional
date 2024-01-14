package com.serviciudad.service;


import com.serviciudad.constantes.Constantes;
import com.serviciudad.entity.AuthModel;
import com.serviciudad.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public final class ListarAprobadosNoConfirmadosService {
    @Autowired
    AuthRepository authRepository;

    @Autowired
    private FacturaEvertecService facturaEvertecService;

    @Autowired
    private ConsultaExisteRecaudoSicepService consultaExisteRecaudoSicepService;

    @Autowired
    private Environment env;

    public List<AuthModel> listarAprobadosNoConfirmados() {
        List<AuthModel> l =  authRepository.findByEstadoPagoConfirmado(Constantes.APPROVED, "N", "%");
        List<AuthModel> authModels = new ArrayList<>();
        l.forEach(authModel -> {
            try {
                String existeSicep = consultaExisteRecaudoSicepService.existePagoEnBaseRecaudo(authModel.getCuenta(), authModel.getReference());
                String existeTemporal = facturaEvertecService.existePagoEnBaseRecaudo(
                                                                    authModel.getCuenta(),
                                                                    authModel.getReference(),
                                                                    env.getProperty("CODIGOBANCOPLACETOPAY"));

                if (existeSicep.equals("N") && existeTemporal.equals("N")) {
                    authModels.add(authModel);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return authModels;
    }
}
