package com.todogrifos.productosms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {

    private Long id;

    private String sku;

    private String nombre;

    private String descripcion;

    private BigDecimal precio;

    private Boolean activo;

    private Integer garantiaMeses;

    private String marca;

    private String categoria;

}
