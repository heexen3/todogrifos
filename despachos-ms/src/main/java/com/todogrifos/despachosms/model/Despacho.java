package com.todogrifos.despachosms.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "despachos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Despacho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Código de seguimiento único para que el cliente rastree su despacho (ej: TRK-99213)
    @Column(name = "codigo_seguimiento", nullable = false, unique = true, length = 30)
    private String codigoSeguimiento;

    // Enlace lógico referencial con el ID de la boleta en ventas-ms
    @Column(name = "venta_id", nullable = false, unique = true)
    private Long ventaId;

    @Column(nullable = false, length = 200)
    private String direccionEnvio;

    @Column(length = 50)
    private String comuna;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoDespacho estado;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_entrega_estimada")
    private LocalDateTime fechaEntregaEstimada;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        // Por defecto,todos los despachos nacen en estado PENDIENTE
        if (this.estado == null) {
            this.estado = EstadoDespacho.PENDIENTE;
        }
    }
}