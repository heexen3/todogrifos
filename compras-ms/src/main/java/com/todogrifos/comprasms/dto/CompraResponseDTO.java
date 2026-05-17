package com.todogrifos.comprasms.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompraResponseDTO {
    private Long id;
    private String numeroFactura;
    private LocalDateTime fechaCompra;
    private String proveedor;
    private Double total;
    private List<CompraDetalleResponseDTO> detalles;
}