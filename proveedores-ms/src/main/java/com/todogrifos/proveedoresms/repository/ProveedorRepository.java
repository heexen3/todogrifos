package com.todogrifos.proveedoresms.repository;

import com.todogrifos.proveedoresms.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    Optional<Proveedor> findByRut(String rut);
    boolean existsByRut(String rut);
    boolean existsByEmail(String email);
}