package com.todogrifos.proveedoresms.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProveedorCreateDTO {

    @NotBlank(message = "El RUT del proveedor es obligatorio.")
    @Pattern(
            regexp = "^[0-9]{1,2}\\.[0-9]{3}\\.[0-9]{3}-[0-9kK]{1}$",
            message = "El formato del RUT es inválido. Debe incluir puntos y guión (ej: 76.123.456-K)."
    )
    private String rut;

    @NotBlank(message = "La razón social del proveedor es obligatoria.")
    @Size(min = 3, max = 100, message = "La razón social debe tener entre 3 y 100 caracteres.")
    private String razonSocial;

    @NotBlank(message = "El correo electrónico es obligatorio.")
    @Email(message = "El formato del correo electrónico corporativo es inválido.")
    @Size(max = 100, message = "El email no puede superar los 100 caracteres.")
    private String email;

    @Size(max = 20, message = "El teléfono no puede superar los 20 caracteres.")
    private String telefono;

    @Size(max = 150, message = "La dirección de la casa matriz no puede superar los 150 caracteres.")
    private String direccion;
}