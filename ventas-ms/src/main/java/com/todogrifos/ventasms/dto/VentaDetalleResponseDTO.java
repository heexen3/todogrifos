package com.todogrifos.ventasms.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VentaDetalleResponseDTO {
    private Long id;
    private String sku;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;
}