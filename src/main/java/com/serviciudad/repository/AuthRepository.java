package com.serviciudad.repository;

import com.serviciudad.entity.AuthModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthRepository extends CrudRepository<AuthModel, String> {

    @Query("Select b from AuthModel b where b.cuenta = :cuenta and b.reference = :reference and b.estado = :estado")
    public AuthModel findByCuentaAndReferenceEstado(@Param("cuenta") String title,
                                                    @Param("reference") String reference,
                                                    @Param("estado") String estado);

    @Query("Select b from AuthModel b where b.cuenta = :cuenta and b.reference = :reference")
    public List<AuthModel> findByCuentaAndReference(@Param("cuenta") String cuenta,
                                                    @Param("reference") String reference);

    @Query("Select b from AuthModel b where b.reference = :reference")
    public List<AuthModel> findByReference(@Param("reference") String reference);

    @Query("Select b from AuthModel b where id = :id")
    public AuthModel findByiD(@Param("id") String id);

    @Query("Select b from AuthModel b where b.estado = :estado")
    public List<AuthModel> findByEstado(@Param("estado") String estado);

    @Query("Select b from AuthModel b where b.requestid = :requestid and b.reference = :reference")
    public AuthModel findByRequestidAndReference(@Param("requestid") int requestid,
                                              @Param("reference") String reference);

    @Query("Select b from AuthModel b where b.estado = :estado and (b.pagoconfirmado = :pagoconfirmado or b.pagoconfirmado is null) and b.fecha like :fecha")
    public List<AuthModel> findByEstadoPagoConfirmado(@Param("estado") String estado,
                                                      @Param("pagoconfirmado") String pagoconfirmado,
                                                      @Param("fecha") String fecha);

    @Query("Select b from AuthModel b where b.cuenta = :cuenta and b.estado = :estado")
    public List<AuthModel> findByCuentaEstado(@Param("cuenta") String cuenta, @Param("estado") String estado);

    @Query("Select b from AuthModel b " +
            "where b.estado = :estado " +
            "and (b.pagoconfirmado = :pagoconfirmado or b.pagoconfirmado is null) " +
            "and b.fecha like :fecha")
    public List<AuthModel> findByEstadoPagoConfirmadoFecha(
            @Param("estado") String estado,
            @Param("pagoconfirmado") String pagoconfirmado,
            @Param("fecha") String fecha);

    @Query("Select b from AuthModel b where b.fecha like ?1%")
    public List<AuthModel> findByFecha(@Param("fecha") String fecha);
}
/*
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2NjU3ODk1MzEsImlhdCI6MTY2NTcwMzEzMSwiaXNzIjoic2VydmljaXVkYWQuZ292LmNvIiwic3ViIjoie1wibG9naW5cIjpcImpvcmdlLmhlbmFvXCIsXCJiYW5jb1wiOlwiMFwiLFwicGVyZmlsXCI6XCJBRE1JTklTVFJBRE9SXCJ9In0.SttsnEBXKiRZtshFy32_BQd13TIoK_JpPF45YMznIIw
 */