package com.todogrifos.inventarioms.repository;


import com.todogrifos.inventarioms.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    Optional<Inventario> findBySku(String sku);

    boolean existsBySku(String sku);
    
}
