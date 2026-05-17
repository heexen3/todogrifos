package com.todogrifos.comprasms.repository;

import com.todogrifos.comprasms.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
    // Permite verificar que no se registre dos veces la misma factura del proveedor
    Optional<Compra> findByNumeroFactura(String numeroFactura);
    boolean existsByNumeroFactura(String numeroFactura);
}