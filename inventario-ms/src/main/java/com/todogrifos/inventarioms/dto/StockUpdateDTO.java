package com.todogrifos.inventarioms.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockUpdateDTO {
    
    private String sku;

    @NotNull(message = "La cantidad modificada es obligatoria")
    // Quitamos la restricción @Min(1) si es que tu lógica de negocio
    // permite valores negativos para restar stock (ej: salidas de inventario)
    private Integer cantidadModificada;
}