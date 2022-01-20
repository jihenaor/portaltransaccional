package com.serviciudad.repository;

import com.serviciudad.entity.AuthModel;
import com.serviciudad.entity.CronModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CronRepository extends CrudRepository<CronModel, String> {


}
