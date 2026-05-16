package com.todogrifos.ventasms.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VentaDetalleCreateDTO {

    @NotBlank(message = "El SKU del producto es obligatorio.")
    private String sku;

    @NotNull(message = "La cantidad es obligatoria.")
    @Min(value = 1, message = "La cantidad mínima de venta debe ser de al menos 1 unidad.")
    private Integer cantidad;

    @NotNull(message = "El precio unitario es obligatorio.")
    @Min(value = 0, message = "El precio unitario no puede ser un valor negativo.")
    private Double precioUnitario;
}