package com.todogrifos.despachosms.service;

import com.todogrifos.despachosms.dto.DespachoCreateDTO;
import com.todogrifos.despachosms.dto.DespachoResponseDTO;
import com.todogrifos.despachosms.dto.DespachoStatusUpdateDTO;

import java.util.List;

public interface DespachoService {
    DespachoResponseDTO crearDespacho(DespachoCreateDTO dto);
    DespachoResponseDTO actualizarEstado(Long id, DespachoStatusUpdateDTO dto);
    List<DespachoResponseDTO> obtenerTodos();
    DespachoResponseDTO obtenerPorTracking(String codigoSeguimiento);
    DespachoResponseDTO obtenerPorVentaId(Long ventaId);
}