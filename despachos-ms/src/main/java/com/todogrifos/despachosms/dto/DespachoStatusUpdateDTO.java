package com.todogrifos.despachosms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DespachoStatusUpdateDTO {

    @NotBlank(message = "El nuevo estado del despacho es obligatorio.")
    // Restringe los valores de entrada estrictamente a los nombres del Enum
    @Pattern(
            regexp = "^(PENDIENTE|EN_RUTA|ENTREGADO)$",
            message = "Estado inválido. Los únicos estados permitidos por la lógica de negocio son: PENDIENTE, EN_RUTA o ENTREGADO."
    )
    private String estado;
}