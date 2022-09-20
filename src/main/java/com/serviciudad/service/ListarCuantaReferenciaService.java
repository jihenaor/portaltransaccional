package com.serviciudad.service;


import com.serviciudad.constantes.Constantes;
import com.serviciudad.entity.AuthModel;
import com.serviciudad.entity.CuentaModel;
import com.serviciudad.entity.ReferenciaModel;
import com.serviciudad.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public final class ListarCuantaReferenciaService {
    @Autowired
    AuthRepository authRepository;

    public List<AuthModel> listar(CuentaModel cuentaModel, ReferenciaModel referenciaModel) {
        return  authRepository.findByCuentaAndReference(cuentaModel.getValue(), referenciaModel.getValue());
    }
}
