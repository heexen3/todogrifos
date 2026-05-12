package com.todogrifos.productosms.repository;

import com.todogrifos.productosms.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
