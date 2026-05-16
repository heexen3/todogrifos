package com.todogrifos.clientesms.service;

import com.todogrifos.clientesms.dto.ClienteCreateDTO;
import com.todogrifos.clientesms.dto.ClienteResponseDTO;

import java.util.List;

public interface ClienteService {
    ClienteResponseDTO registrarCliente(ClienteCreateDTO dto);
    ClienteResponseDTO obtenerPorRut(String rut);
    ClienteResponseDTO obtenerPorId(Long id);
    List<ClienteResponseDTO> obtenerTodos();
}