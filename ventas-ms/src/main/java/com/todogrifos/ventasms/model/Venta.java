package com.todogrifos.ventasms.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ventas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Folio o número único de boleta comercial
    @Column(nullable = false, unique = true, length = 20)
    private String folio;

    @Column(name = "fecha_venta", nullable = false)
    private LocalDateTime fechaVenta;

    // Guardamos el ID del cliente para mantener la integridad lógica referencial con clientes-ms
    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @Column(nullable = false)
    private Double total;

    // Relación obligatoria por rúbrica: Una Venta tiene muchos Detalles (Maestro-Detalle)
    // El 'cascade = CascadeType.ALL' asegura que al guardar la Venta se guarden sus detalles en cadena
    // 'orphanRemoval = true' elimina de la BD los detalles que se quiten de esta lista
    @Builder.Default
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<VentaDetalle> detalles = new ArrayList<>();

    /**
     * Metodo de conveniencia (Helper) fundamental en relaciones bidireccionales
     * para asegurar que ambos lados de la relación estén perfectamente sincronizados.
     */
    public void agregarDetalle(VentaDetalle detalle) {
        if (detalles == null) {
            detalles = new ArrayList<>();
        }
        detalles.add(detalle);
        detalle.setVenta(this); // Setea automáticamente la clave foránea hacia este objeto maestro
    }
}