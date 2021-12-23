package com.serviciudad.repository;

import com.serviciudad.model.ErrorModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrorRepository extends CrudRepository<ErrorModel, String> {

}
