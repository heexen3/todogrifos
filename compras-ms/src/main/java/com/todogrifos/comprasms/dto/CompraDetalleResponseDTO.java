package com.todogrifos.comprasms.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompraDetalleResponseDTO {
    private Long id;
    private String sku;
    private Integer cantidad;
    private Double precioCosto;
    private Double subtotal;
}