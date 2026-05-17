package com.todogrifos.devolucionesms.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DevolucionCreateDTO {

    @NotBlank(message = "El folio de la Nota de Crédito es obligatorio.")
    @Pattern(
            regexp = "^NCG-[0-9]{4}-[0-9]{4}$",
            message = "El formato del folio es inválido. Debe seguir el patrón institucional (ej: NCG-2026-0001)."
    )
    private String notaCreditoFolio;

    @NotNull(message = "El ID de la venta original es obligatorio para cursar la devolución.")
    private Long ventaId;

    @NotBlank(message = "El SKU del artículo a devolver es obligatorio.")
    @Size(max = 50, message = "El SKU no puede superar los 50 caracteres.")
    private String sku;

    @NotNull(message = "La cantidad de artículos a devolver es obligatoria.")
    @Positive(message = "La cantidad a devolver debe ser un número entero mayor a cero.")
    private Integer cantidad;

    @NotBlank(message = "El motivo o justificación técnica de la devolución es obligatorio.")
    @Size(min = 5, max = 250, message = "El motivo debe ser explícito (entre 5 y 250 caracteres).")
    private String motivo;

    @NotBlank(message = "El destino logístico del producto físico es obligatorio.")
    @Pattern(
            regexp = "^(REINGRESADO_A_STOCK|ENVIADO_A_MERMA)$",
            message = "Destino logístico inválido. Valores permitidos: REINGRESADO_A_STOCK o ENVIADO_A_MERMA."
    )
    private String destinoLogistico;
}