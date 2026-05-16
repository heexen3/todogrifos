package com.todogrifos.ventasms.service;

import com.todogrifos.ventasms.dto.VentaCreateDTO;
import com.todogrifos.ventasms.dto.VentaResponseDTO;

import java.util.List;

public interface VentaService {
    VentaResponseDTO procesarVenta(VentaCreateDTO dto);
    List<VentaResponseDTO> obtenerTodas();
}