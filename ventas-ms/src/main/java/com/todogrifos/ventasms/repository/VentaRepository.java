package com.todogrifos.ventasms.repository;

import com.todogrifos.ventasms.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    // Permite verificar si un folio de boleta ya fue emitido previamente
    Optional<Venta> findByFolio(String folio);
    boolean existsByFolio(String folio);
}