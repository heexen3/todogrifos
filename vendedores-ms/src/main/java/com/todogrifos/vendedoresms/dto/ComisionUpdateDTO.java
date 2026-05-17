package com.todogrifos.vendedoresms.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComisionUpdateDTO {

    @NotNull(message = "El monto base de la venta es obligatorio para calcular la comisión.")
    @Positive(message = "El monto de la venta debe ser mayor a cero.")
    private Double montoVenta;
}