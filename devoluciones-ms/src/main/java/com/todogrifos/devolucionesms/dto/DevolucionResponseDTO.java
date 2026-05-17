package com.todogrifos.devolucionesms.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DevolucionResponseDTO {
    private Long id;
    private String notaCreditoFolio;
    private Long ventaId;
    private String sku;
    private Integer cantidad;
    private String motivo;
    private String destinoLogistico;
    private LocalDateTime fechaDevolucion;
}