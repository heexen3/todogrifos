package com.todogrifos.vendedoresms.service;

import com.todogrifos.vendedoresms.dto.VendedorCreateDTO;
import com.todogrifos.vendedoresms.dto.VendedorResponseDTO;
import com.todogrifos.vendedoresms.dto.ComisionUpdateDTO;

import java.util.List;

public interface VendedorService {
    VendedorResponseDTO crearVendedor(VendedorCreateDTO dto);
    VendedorResponseDTO acumularComision(Long id, ComisionUpdateDTO dto);
    List<VendedorResponseDTO> obtenerTodos();
    VendedorResponseDTO obtenerPorId(Long id);
    VendedorResponseDTO obtenerPorCodigo(String codigoInterno);
}