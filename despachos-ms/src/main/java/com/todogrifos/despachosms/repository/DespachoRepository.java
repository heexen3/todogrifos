package com.todogrifos.despachosms.repository;

import com.todogrifos.despachosms.model.Despacho;
import com.todogrifos.despachosms.model.EstadoDespacho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DespachoRepository extends JpaRepository<Despacho, Long> {
    Optional<Despacho> findByCodigoSeguimiento(String codigoSeguimiento);
    Optional<Despacho> findByVentaId(Long ventaId);
    List<Despacho> findByEstado(EstadoDespacho estado);
    boolean existsByCodigoSeguimiento(String codigoSeguimiento);
    boolean existsByVentaId(Long ventaId);
}