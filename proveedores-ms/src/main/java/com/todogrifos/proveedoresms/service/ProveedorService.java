package com.todogrifos.proveedoresms.service;

import com.todogrifos.proveedoresms.dto.ProveedorCreateDTO;
import com.todogrifos.proveedoresms.dto.ProveedorResponseDTO;

import java.util.List;

public interface ProveedorService {
    ProveedorResponseDTO crearProveedor(ProveedorCreateDTO dto);
    List<ProveedorResponseDTO> obtenerTodos();
    ProveedorResponseDTO obtenerPorId(Long id);
    ProveedorResponseDTO obtenerPorRut(String rut);
}