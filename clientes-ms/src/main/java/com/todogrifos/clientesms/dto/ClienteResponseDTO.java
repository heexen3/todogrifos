package com.todogrifos.clientesms.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteResponseDTO {
    private Long id;
    private String rut;
    private String nombreCompleto;
    private String email;
    private String telefono;
    private String direccion;
}