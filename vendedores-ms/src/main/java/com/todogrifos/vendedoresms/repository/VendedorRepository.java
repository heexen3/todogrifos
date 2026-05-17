package com.todogrifos.vendedoresms.repository;

import com.todogrifos.vendedoresms.model.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendedorRepository extends JpaRepository<Vendedor, Long> {

    // Consultas para validar unicidad y enlaces remotos con ventas-ms
    Optional<Vendedor> findByCodigoInterno(String codigoInterno);
    Optional<Vendedor> findByRut(String rut);
    boolean existsByCodigoInterno(String codigoInterno);
    boolean existsByRut(String rut);
}