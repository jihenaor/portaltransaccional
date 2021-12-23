package com.serviciudad.repository;

import com.serviciudad.AuthModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends CrudRepository<AuthModel, String> {

}
