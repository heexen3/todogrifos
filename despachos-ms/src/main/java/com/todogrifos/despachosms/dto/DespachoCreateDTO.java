package com.todogrifos.despachosms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DespachoCreateDTO {

    @NotBlank(message = "El código de seguimiento (Tracking Number) es obligatorio para la orden logística.")
    @Size(max = 30, message = "El código de seguimiento no puede superar los 30 caracteres.")
    private String codigoSeguimiento;

    @NotNull(message = "El ID de la venta asociada es obligatorio.")
    private Long ventaId;

    @NotBlank(message = "La dirección de destino es obligatoria para el despacho.")
    @Size(min = 10, max = 200, message = "La dirección debe ser clara y detallada (entre 10 y 200 caracteres).")
    private String direccionEnvio;

    @NotBlank(message = "La comuna de entrega es obligatoria.")
    @Size(max = 50, message = "El nombre de la comuna no puede superar los 50 caracteres.")
    private String comuna;
}