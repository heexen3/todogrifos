package com.todogrifos.productosms.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El SKU no puede quedar vacío.")
    @Column(unique = true, nullable = false)
    private String sku;

    @NotBlank(message = "El nombre no puede quedar vacío.")
    @Column(nullable = false)
    private String nombre;

    @Column(nullable = true)
    private String descripcion;

    @NotNull(message = "El precio no puede ser 0 ni nulo")
    @Positive
    @Column(nullable = false)
    private BigDecimal precio;

    
    @Column(nullable = false)
    private Boolean activo;

    @Min(0)
    @Column(nullable = true)
    private Integer garantiaMeses;

    @ManyToOne
    @NotNull(message = "La marca es obligatoria.")
    @JoinColumn(name = "marca_id")
    private Marca marca;

    @ManyToOne
    @NotNull(message = "La categoría es obligatoria.")
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;


}
