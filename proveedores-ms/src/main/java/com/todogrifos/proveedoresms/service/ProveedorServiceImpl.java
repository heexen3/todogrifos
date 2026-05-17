package com.todogrifos.proveedoresms.service;

import com.todogrifos.proveedoresms.dto.ProveedorCreateDTO;
import com.todogrifos.proveedoresms.dto.ProveedorResponseDTO;
import com.todogrifos.proveedoresms.exception.ProveedorDuplicadoException;
import com.todogrifos.proveedoresms.exception.ProveedorNotFoundException;
import com.todogrifos.proveedoresms.model.Proveedor;
import com.todogrifos.proveedoresms.repository.ProveedorRepository;
import com.todogrifos.proveedoresms.service.ProveedorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProveedorServiceImpl implements ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Override
    @Transactional
    public ProveedorResponseDTO crearProveedor(ProveedorCreateDTO dto) {
        log.info("Iniciando proceso de registro para el proveedor con RUT: {}", dto.getRut());

        // Validar si el RUT ya está registrado
        if (proveedorRepository.existsByRut(dto.getRut())) {
            log.warn("Fallo de negocio: Intento de registro con RUT duplicado: {}", dto.getRut());
            throw new ProveedorDuplicadoException("Ya existe un proveedor registrado con el RUT: " + dto.getRut());
        }

        // Validar si el Email corporativo ya está registrado
        if (proveedorRepository.existsByEmail(dto.getEmail())) {
            log.warn("Fallo de negocio: El correo electrónico {} ya se encuentra registrado.", dto.getEmail());
            throw new ProveedorDuplicadoException("El correo electrónico '" + dto.getEmail() + "' ya está asociado a otro proveedor.");
        }

        // Construcción de la entidad mediante patrón Builder
        Proveedor proveedor = Proveedor.builder()
                .rut(dto.getRut())
                .razonSocial(dto.getRazonSocial())
                .email(dto.getEmail())
                .telefono(dto.getTelefono())
                .direccion(dto.getDireccion())
                .build();

        Proveedor guardado = proveedorRepository.save(proveedor);
        log.info("Proveedor '{}' registrado exitosamente con ID: {}", guardado.getRazonSocial(), guardado.getId());

        return mapToResponseDTO(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorResponseDTO> obtenerTodos() {
        log.info("Recuperando el listado maestro de proveedores activos.");
        return proveedorRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProveedorResponseDTO obtenerPorId(Long id) {
        log.info("Buscando proveedor por ID: {}", id);
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Búsqueda fallida: No se encontró proveedor con ID: {}", id);
                    return new ProveedorNotFoundException("Proveedor no encontrado con el ID especificado: " + id);
                });
        return mapToResponseDTO(proveedor);
    }

    @Override
    @Transactional(readOnly = true)
    public ProveedorResponseDTO obtenerPorRut(String rut) {
        log.info("Buscando proveedor por RUT: {}", rut);
        Proveedor proveedor = proveedorRepository.findByRut(rut)
                .orElseThrow(() -> {
                    log.warn("Búsqueda fallida: No existe proveedor con RUT: {}", rut);
                    return new ProveedorNotFoundException("No se encontró ningún proveedor registrado con el RUT: " + rut);
                });
        return mapToResponseDTO(proveedor);
    }

    /**
     * Mapeador interno para transformar la entidad en un DTO limpio de salida.
     */
    private ProveedorResponseDTO mapToResponseDTO(Proveedor proveedor) {
        return ProveedorResponseDTO.builder()
                .id(proveedor.getId())
                .rut(proveedor.getRut())
                .razonSocial(proveedor.getRazonSocial())
                .email(proveedor.getEmail())
                .telefono(proveedor.getTelefono())
                .direccion(proveedor.getDireccion())
                .fechaRegistro(proveedor.getFechaRegistro())
                .build();
    }
}