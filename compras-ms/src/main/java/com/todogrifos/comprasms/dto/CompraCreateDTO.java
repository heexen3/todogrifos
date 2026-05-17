package com.todogrifos.comprasms.dto;

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
public class CompraCreateDTO {

    @NotBlank(message = "El número de factura o documento del proveedor es obligatorio.")
    @Size(max = 30, message = "El número de factura no puede superar los 30 caracteres.")
    private String numeroFactura;

    @NotBlank(message = "El nombre o razón social del proveedor es obligatorio.")
    @Size(max = 100, message = "El nombre del proveedor no puede superar los 100 caracteres.")
    private String proveedor;

    @NotEmpty(message = "La orden de compra debe incluir obligatoriamente al menos un artículo en su detalle.")
    @Valid // REQUISITO RÚBRICA: Ejecuta recursivamente las anotaciones de validación en la lista interna
    private List<CompraDetalleCreateDTO> detalles;
}