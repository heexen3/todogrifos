package com.todogrifos.devolucionesms.service;

import com.todogrifos.devolucionesms.dto.DevolucionCreateDTO;
import com.todogrifos.devolucionesms.dto.DevolucionResponseDTO;

import java.util.List;

public interface DevolucionService {
    DevolucionResponseDTO procesarDevolucion(DevolucionCreateDTO dto);
    List<DevolucionResponseDTO> obtenerTodas();
    List<DevolucionResponseDTO> obtenerPorVentaId(Long ventaId);
}