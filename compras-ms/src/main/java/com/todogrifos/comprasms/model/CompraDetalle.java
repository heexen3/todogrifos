package com.todogrifos.comprasms.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "compra_detalles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompraDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // REQUISITO RÚBRICA: Muchos detalles corresponden a una única cabecera de compra.
    // Configura explícitamente el nombre de la FK y la integridad referencial en la base de datos.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compra_id", nullable = false, foreignKey = @ForeignKey(name = "FK_DETALLE_COMPRA"))
    private Compra compra;

    // Código SKU del producto que se inyectará en inventario-ms
    @Column(nullable = false, length = 50)
    private String sku;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_costo", nullable = false)
    private Double precioCosto;

    @Column(nullable = false)
    private Double subtotal;
}