package com.todogrifos.inventarioms.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class InventarioResponseDTO {
    private Long id;
    private String sku;
    private Integer cantidad;
    private Integer stockMinimo;
    private boolean enStock;
}