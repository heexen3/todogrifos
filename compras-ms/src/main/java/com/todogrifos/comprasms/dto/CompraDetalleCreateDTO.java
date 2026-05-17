package com.todogrifos.comprasms.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompraDetalleCreateDTO {

    @NotBlank(message = "El SKU del producto es obligatorio para el reabastecimiento.")
    private String sku;

    @NotNull(message = "La cantidad comprada es obligatoria.")
    @Min(value = 1, message = "La cantidad mínima de compra debe ser de al menos 1 unidad.")
    private Integer cantidad;

    @NotNull(message = "El precio de costo es obligatorio.")
    @Min(value = 0, message = "El precio de costo por unidad no puede ser negativo.")
    private Double precioCosto;
}