package com.todogrifos.productosms.repository;


import com.todogrifos.productosms.model.Producto;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    List<Producto> findByMarca_Id(Long marcaId);

    List<Producto> findByCategoria_Id(Long categoriaId);

    List<Producto> findByActivoTrue();

    Optional<Producto> findBySku(String sku);

    boolean existsBySku(String sku);
}
