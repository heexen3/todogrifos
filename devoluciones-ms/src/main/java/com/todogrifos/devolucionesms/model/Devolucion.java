package com.todogrifos.devolucionesms.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "devoluciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Devolucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Código único institucional de la Nota de Crédito emitida (ej: NCG-2026-0001)
    @Column(name = "nota_credito_folio", nullable = false, unique = true, length = 30)
    private String notaCreditoFolio;

    // Enlace lógico referencial hacia el ID de la boleta en ventas-ms
    @Column(name = "venta_id", nullable = false)
    private Long ventaId;

    // SKU del artículo que se está retornando
    @Column(nullable = false, length = 50)
    private String sku;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false, length = 250)
    private String motivo;

    // mapeo del destino físico como un String en BD
    @Enumerated(EnumType.STRING)
    @Column(name = "destino_logistico", nullable = false, length = 30)
    private DestinoDevolucion destinoLogistico;

    @Column(name = "fecha_devolucion", nullable = false)
    private LocalDateTime fechaDevolucion;

    @PrePersist
    protected void onCreate() {
        this.fechaDevolucion = LocalDateTime.now();
    }
}