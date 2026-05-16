package com.todogrifos.ventasms.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VentaResponseDTO {
    private Long id;
    private String folio;
    private LocalDateTime fechaVenta;
    private Long clienteId;
    private Double total;
    private List<VentaDetalleResponseDTO> detalles;
}