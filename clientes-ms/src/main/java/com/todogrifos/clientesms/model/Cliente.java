package com.todogrifos.clientesms.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 15)
    private String rut;

    @Column(nullable = false, length = 50)
    private String nombres;

    @Column(name = "ap_paterno", nullable = false, length = 50)
    private String appaterno;

    @Column(name = "ap_materno", nullable = false, length = 50)
    private String apmaterno;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 20)
    private String telefono;

    @Column(length = 150)
    private String direccion;
}