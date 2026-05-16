package com.todogrifos.clientesms.service;

import com.todogrifos.clientesms.dto.ClienteCreateDTO;
import com.todogrifos.clientesms.dto.ClienteResponseDTO;
import com.todogrifos.clientesms.exception.ClienteDuplicadoException;
import com.todogrifos.clientesms.exception.ClienteNotFoundException;
import com.todogrifos.clientesms.model.Cliente;
import com.todogrifos.clientesms.repository.ClienteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public ClienteResponseDTO registrarCliente(ClienteCreateDTO dto) {
        log.info("Iniciando proceso de registro para el cliente con RUT: {}", dto.getRut());

        if (clienteRepository.existsByRut(dto.getRut())) {
            log.warn("Fallo al registrar cliente: El RUT {} ya se encuentra registrado en el sistema.", dto.getRut());
            throw new ClienteDuplicadoException("Ya existe un cliente registrado con el RUT: " + dto.getRut());
        }

        if (clienteRepository.existsByEmail(dto.getEmail())) {
            log.warn("Fallo al registrar cliente: El correo electrónico {} ya está en uso.", dto.getEmail());
            throw new ClienteDuplicadoException("Ya existe un cliente registrado con el correo electrónico: " + dto.getEmail());
        }

        Cliente cliente = Cliente.builder()
                .rut(dto.getRut())
                .nombres(dto.getNombres())
                .appaterno(dto.getAppaterno())
                .apmaterno(dto.getApmaterno())
                .email(dto.getEmail())
                .telefono(dto.getTelefono())
                .direccion(dto.getDireccion())
                .build();

        Cliente clienteGuardado = clienteRepository.save(cliente);
        log.info("Cliente registrado exitosamente con ID asignado: {} y RUT: {}", clienteGuardado.getId(), clienteGuardado.getRut());

        return mapToResponseDTO(clienteGuardado);
    }

    @Override
    public ClienteResponseDTO obtenerPorRut(String rut) {
        log.info("Buscando cliente en el sistema con RUT: {}", rut);

        Cliente cliente = clienteRepository.findByRut(rut)
                .orElseThrow(() -> {
                    log.warn("No se encontró ningún cliente activo con el RUT: {}", rut);
                    return new ClienteNotFoundException("Cliente no encontrado con el RUT: " + rut);
                });

        return mapToResponseDTO(cliente);
    }

    @Override
    public ClienteResponseDTO obtenerPorId(Long id) {
        log.info("Buscando cliente con ID interno: {}", id);

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se encontró ningún registro para el cliente con ID: {}", id);
                    return new ClienteNotFoundException("Cliente no encontrado con el ID: " + id);
                });

        return mapToResponseDTO(cliente);
    }

    @Override
    public List<ClienteResponseDTO> obtenerTodos() {
        log.info("Solicitando listado completo de todos los clientes registrados.");

        List<Cliente> clientes = clienteRepository.findAll();
        log.info("Se recuperaron {} registros de clientes desde la base de datos.", clientes.size());

        return clientes.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private ClienteResponseDTO mapToResponseDTO(Cliente cliente) {
        // concatenación ordenada para construir un formato limpio de Nombre Completo comercial
        String nombreCompleto = String.format("%s %s %s",
                cliente.getNombres(),
                cliente.getAppaterno(),
                cliente.getApmaterno());

        return ClienteResponseDTO.builder()
                .id(cliente.getId())
                .rut(cliente.getRut())
                .nombreCompleto(nombreCompleto.trim())
                .email(cliente.getEmail())
                .telefono(cliente.getTelefono())
                .direccion(cliente.getDireccion())
                .build();
    }
}