package com.todogrifos.ventasms.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VentaCreateDTO {

    @NotBlank(message = "El folio de la boleta es obligatorio.")
    @Size(max = 20, message = "El folio no puede superar los 20 caracteres.")
    private String folio;

    @NotNull(message = "El ID del cliente es obligatorio.")
    private Long clienteId;

    @NotNull(message = "El ID del vendedor es obligatorio.")
    private Long vendedorId;

    @NotEmpty(message = "El detalle de la venta no puede estar vacío, debe incluir al menos un artículo.")
    @Valid
    private List<VentaDetalleCreateDTO> detalles;
}