package com.serviciudad.repository;

import com.serviciudad.entity.AuthModel;
import com.serviciudad.entity.RequestModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends CrudRepository<RequestModel, String> {


}
