package com.todogrifos.despachosms.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DespachoResponseDTO {
    private Long id;
    private String codigoSeguimiento;
    private Long ventaId;
    private String direccionEnvio;
    private String comuna;
    private String estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaEntregaEstimada;
}