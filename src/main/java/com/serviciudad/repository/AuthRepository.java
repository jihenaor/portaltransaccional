package com.serviciudad.repository;

import com.serviciudad.model.AuthModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthRepository extends CrudRepository<AuthModel, String> {
    AuthModel findByCuentaAndReference(String cuenta, String reference);
}
