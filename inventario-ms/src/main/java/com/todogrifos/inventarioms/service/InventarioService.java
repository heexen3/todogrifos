package com.todogrifos.inventarioms.service;

import com.todogrifos.inventarioms.dto.InventarioCreateDTO;
import com.todogrifos.inventarioms.dto.InventarioResponseDTO;
import com.todogrifos.inventarioms.dto.StockUpdateDTO;

import java.util.List;

public interface InventarioService {
    InventarioResponseDTO crearInventario(InventarioCreateDTO dto);
    InventarioResponseDTO obtenerPorSku(String sku);
    List<InventarioResponseDTO> obtenerTodo();
    InventarioResponseDTO agregarStock(String sku, StockUpdateDTO dto);
    InventarioResponseDTO retirarStock(String sku, StockUpdateDTO dto);
    boolean validarDisponibilidad(String sku, Integer cantidad);
}