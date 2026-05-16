package com.todogrifos.ventasms.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "venta_detalles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VentaDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // muchos detalles pertenecen a una única venta
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id", nullable = false, foreignKey = @ForeignKey(name = "FK_DETALLE_VENTA"))
    private Venta venta;

    // identificador del producto para cruzar lógicamente con el catálogo e inventario
    @Column(nullable = false, length = 50)
    private String sku;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false)
    private Double precioUnitario;

    @Column(nullable = false)
    private Double subtotal;
}