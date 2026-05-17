package com.todogrifos.proveedoresms.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProveedorResponseDTO {
    private Long id;
    private String rut;
    private String razonSocial;
    private String email;
    private String telefono;
    private String direccion;
    private LocalDateTime fechaRegistro;
}