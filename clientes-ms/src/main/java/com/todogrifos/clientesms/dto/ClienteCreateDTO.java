package com.todogrifos.clientesms.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteCreateDTO {

    @NotBlank(message = "El RUT es obligatorio.")
    @Size(min = 8, max = 15, message = "El RUT debe tener entre 8 y 15 caracteres.")
    private String rut;

    @NotBlank(message = "El nombre es obligatorio.")
    @Size(max = 50, message = "El nombre no puede superar los 50 caracteres.")
    private String nombres;

    @NotBlank(message = "El apellido paterno es obligatorio.")
    @Size(max = 50, message = "El apellido paterno no puede superar los 50 caracteres.")
    private String appaterno;

    @NotBlank(message = "El apellido materno es obligatorio.")
    @Size(max = 50, message = "El apellido materno no puede superar los 50 caracteres.")
    private String apmaterno;

    @NotBlank(message = "El correo electrónico es obligatorio.")
    @Email(message = "El formato del correo electrónico es inválido.")
    @Size(max = 100, message = "El correo no puede superar los 100 caracteres.")
    private String email;

    private String telefono;
    private String direccion;
}