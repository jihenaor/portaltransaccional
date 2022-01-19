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

    @Query("Select b from AuthModel b where b.cuenta = :cuenta and b.reference = :reference")
    public AuthModel findByCuentaAndReference(@Param("cuenta") String title,
                                  @Param("reference") String reference);

    @Query("Select b from AuthModel b where id = :id")
    public AuthModel findByiD(@Param("id") String id);

    @Query("Select b from AuthModel b where b.estado = :estado")
    public List<AuthModel> findByEstado(@Param("estado") String estado);
}
