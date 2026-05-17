package com.todogrifos.comprasms.service;

import com.todogrifos.comprasms.dto.CompraCreateDTO;
import com.todogrifos.comprasms.dto.CompraResponseDTO;

import java.util.List;

public interface CompraService {
    CompraResponseDTO procesarCompra(CompraCreateDTO dto);
    List<CompraResponseDTO> obtenerTodas();
}