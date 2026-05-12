package com.todogrifos.productosms.dto;

import com.todogrifos.productosms.model.Categoria;
import com.todogrifos.productosms.model.Marca;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductoUpdateDTO {
    @NotBlank(message = "El nombre no puede quedar vacío.")
    private String nombre;

    private String descripcion;

    @NotNull(message = "El precio es obligatorio.")
    @Positive(message = "El precio debe ser mayor a 0.")
    private BigDecimal precio;

    private Boolean activo;

    @Min(value = 0, message = "La garantía no puede ser negativa.")
    private Integer garantiaMeses;
}