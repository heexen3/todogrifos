package com.todogrifos.vendedoresms.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendedorCreateDTO {

    @NotBlank(message = "El código interno del empleado es obligatorio.")
    @Size(min = 4, max = 20, message = "El código interno debe tener entre 4 and 20 caracteres (ej: VEND-001).")
    private String codigoInterno;

    @NotBlank(message = "El RUT del vendedor es obligatorio.")
    @Pattern(
            regexp = "^[0-9]{1,2}\\.[0-9]{3}\\.[0-9]{3}-[0-9kK]{1}$",
            message = "El formato del RUT es inválido. Debe incluir puntos y guión (ej: 12.345.678-9)."
    )
    private String rut;

    @NotBlank(message = "El nombre completo del vendedor es obligatorio.")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 and 100 caracteres.")
    private String nombre;

    @NotBlank(message = "La sucursal física asignada es obligatoria.")
    @Size(max = 50, message = "El nombre de la sucursal no puede superar los 50 caracteres.")
    private String sucursal;

    @NotNull(message = "El porcentaje de comisión es obligatorio.")
    @DecimalMin(value = "0.0", message = "El porcentaje de comisión no puede ser negativo.")
    @DecimalMax(value = "20.0", message = "El porcentaje de comisión no puede superar el 20.0% por motivos de política interna.")
    private Double porcentajeComision;
}