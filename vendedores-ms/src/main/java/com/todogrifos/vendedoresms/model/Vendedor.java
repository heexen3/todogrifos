package com.todogrifos.vendedoresms.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "vendedores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vendedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Código identificador interno del empleado en TodoGrifos (ej: VEND-001)
    @Column(name = "codigo_interno", nullable = false, unique = true, length = 20)
    private String codigoInterno;

    // RUT o documento de identidad del trabajador
    @Column(nullable = false, unique = true, length = 15)
    private String rut;

    @Column(nullable = false, length = 100)
    private String nombre;

    // Sucursal física asignada (ej: Estación Central, Santiago Centro)
    @Column(nullable = false, length = 50)
    private String sucursal;

    // Porcentaje de comisión fijo asignado al vendedor (ej: 2.5 para un 2.5%)
    @Column(name = "porcentaje_comision", nullable = false)
    private Double porcentajeComision;

    // Monto total en dinero acumulado por las comisiones del mes
    @Column(name = "comision_acumulada", nullable = false)
    private Double comisionAcumulada;

    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDateTime fechaIngreso;

    @PrePersist
    protected void onCreate() {
        this.fechaIngreso = LocalDateTime.now();
        // Al registrar un vendedor nuevo, su comisión acumulada parte lógicamente en $0
        if (this.comisionAcumulada == null) {
            this.comisionAcumulada = 0.0;
        }
    }
}