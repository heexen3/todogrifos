package com.todogrifos.vendedoresms.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendedorResponseDTO {
    private Long id;
    private String codigoInterno;
    private String rut;
    private String nombre;
    private String sucursal;
    private Double porcentajeComision;
    private Double comisionAcumulada;
    private LocalDateTime fechaIngreso;
}