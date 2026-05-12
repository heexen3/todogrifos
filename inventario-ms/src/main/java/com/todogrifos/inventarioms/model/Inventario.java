package com.todogrifos.inventarioms.model;

import lombok.*;
import jakarta.persistence.*;



@Entity
@Table(name = "inventarios")
@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Inventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String sku; // puente con productos-ms

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "stock_minimo")
    private Integer stockMinimo;
}
