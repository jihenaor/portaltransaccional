package com.serviciudad.repository;

import com.serviciudad.model.AuthModel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthRepository extends CrudRepository<AuthModel, String> {

    @Query("Select b from AuthModel b where b.cuenta = :cuenta and b.reference = :reference and estado = :estado")
    public AuthModel findByCuentaAndReferenceEstado(@Param("cuenta") String title,
                                  @Param("reference") String reference,
                                  @Param("estado") String estado);
}
