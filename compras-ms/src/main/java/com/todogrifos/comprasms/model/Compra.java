package com.todogrifos.comprasms.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "compras")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Número identificador único de la Factura u Orden de Compra del Proveedor
    @Column(name = "numero_factura", nullable = false, unique = true, length = 30)
    private String numeroFactura;

    @Column(name = "fecha_compra", nullable = false)
    private LocalDateTime fechaCompra;

    // Identificación o nombre del Proveedor que distribuye los insumos
    @Column(nullable = false, length = 100)
    private String proveedor;

    @Column(nullable = false)
    private Double total;

    // REQUISITO RÚBRICA: Una compra agrupa múltiples líneas de detalle en cascada completa.
    // 'mappedBy' indica el campo que posee la relación física en la clase hija.
    @Builder.Default
    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CompraDetalle> detalles = new ArrayList<>();

    /**
     * Helper metodológico fundamental en relaciones bidireccionales de JPA.
     * Sincroniza ambos lados de la relación en memoria antes de persistir.
     */
    public void agregarDetalle(CompraDetalle detalle) {
        if (detalles == null) {
            detalles = new ArrayList<>();
        }
        detalles.add(detalle);
        detalle.setCompra(this); // Setea de forma automática la FK hacia este maestro
    }
}