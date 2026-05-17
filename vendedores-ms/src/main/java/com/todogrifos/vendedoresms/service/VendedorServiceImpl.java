package com.todogrifos.vendedoresms.service;

import com.todogrifos.vendedoresms.dto.VendedorCreateDTO;
import com.todogrifos.vendedoresms.dto.VendedorResponseDTO;
import com.todogrifos.vendedoresms.dto.ComisionUpdateDTO;
import com.todogrifos.vendedoresms.exception.VendedorDuplicadoException;
import com.todogrifos.vendedoresms.exception.VendedorNotFoundException;
import com.todogrifos.vendedoresms.model.Vendedor;
import com.todogrifos.vendedoresms.repository.VendedorRepository;
import com.todogrifos.vendedoresms.service.VendedorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class VendedorServiceImpl implements VendedorService {

    @Autowired
    private VendedorRepository vendedorRepository;

    @Override
    @Transactional
    public VendedorResponseDTO crearVendedor(VendedorCreateDTO dto) {
        log.info("Iniciando alta de personal de ventas. Código asignado: {}, RUT: {}",
                dto.getCodigoInterno(), dto.getRut());

        // Validar si el código interno de empleado ya existe
        if (vendedorRepository.existsByCodigoInterno(dto.getCodigoInterno())) {
            log.warn("Fallo de negocio: El código de empleado '{}' ya está registrado.", dto.getCodigoInterno());
            throw new VendedorDuplicadoException("El código interno '" + dto.getCodigoInterno() + "' ya pertenece a otro trabajador.");
        }

        // Validar si el RUT ya existe en los contratos
        if (vendedorRepository.existsByRut(dto.getRut())) {
            log.warn("Fallo de negocio: El RUT '{}' ya se encuentra registrado en el sistema.", dto.getRut());
            throw new VendedorDuplicadoException("Ya existe un registro de personal asociado al RUT: " + dto.getRut());
        }

        // Mapeo e inicialización limpia
        Vendedor vendedor = Vendedor.builder()
                .codigoInterno(dto.getCodigoInterno())
                .rut(dto.getRut())
                .nombre(dto.getNombre())
                .sucursal(dto.getSucursal())
                .porcentajeComision(dto.getPorcentajeComision())
                .comisionAcumulada(0.0) // Nace lógicamente en cero
                .build();

        Vendedor guardado = vendedorRepository.save(vendedor);
        log.info("Vendedor '{}' registrado con éxito. ID: {}, Comisión asignada: {}%",
                guardado.getNombre(), guardado.getId(), guardado.getPorcentajeComision());

        return mapToResponseDTO(guardado);
    }

    @Override
    @Transactional
    public VendedorResponseDTO acumularComision(Long id, ComisionUpdateDTO dto) {
        log.info("Procesando adición de comisión remota para Vendedor ID: {}. Monto base de venta: ${}",
                id, dto.getMontoVenta());

        Vendedor vendedor = vendedorRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Fallo de adición: No existe el vendedor con ID: {}", id);
                    return new VendedorNotFoundException("No se encontró ningún vendedor registrado con el ID: " + id);
                });

        // LÓGICA COMERCIAL: Calcular el dinero de la comisión basado en su tasa configurada
        double comisionCalculada = dto.getMontoVenta() * (vendedor.getPorcentajeComision() / 100.0);
        double saldoAnterior = vendedor.getComisionAcumulada();
        double nuevoSaldo = saldoAnterior + comisionCalculada;

        log.info("Cálculo comercial efectuado para '{}': Tasa {}% | Dinero ganado: ${} | Balance: ${} -> ${}",
                vendedor.getNombre(), vendedor.getPorcentajeComision(), comisionCalculada, saldoAnterior, nuevoSaldo);

        vendedor.setComisionAcumulada(nuevoSaldo);
        Vendedor actualizado = vendedorRepository.save(vendedor);

        return mapToResponseDTO(actualizado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VendedorResponseDTO> obtenerTodos() {
        log.info("Recuperando nómina completa de ejecutivos de venta.");
        return vendedorRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public VendedorResponseDTO obtenerPorId(Long id) {
        log.info("Buscando vendedor por ID: {}", id);
        Vendedor vendedor = vendedorRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Búsqueda fallida: ID {} inexistente.", id);
                    return new VendedorNotFoundException("Vendedor no encontrado con el ID: " + id);
                });
        return mapToResponseDTO(vendedor);
    }

    @Override
    @Transactional(readOnly = true)
    public VendedorResponseDTO obtenerPorCodigo(String codigoInterno) {
        log.info("Buscando vendedor por código interno de empleado: {}", codigoInterno);
        Vendedor vendedor = vendedorRepository.findByCodigoInterno(codigoInterno)
                .orElseThrow(() -> {
                    log.warn("Búsqueda fallida: Código interno '{}' no registrado.", codigoInterno);
                    return new VendedorNotFoundException("No se registra personal con el código de empleado: " + codigoInterno);
                });
        return mapToResponseDTO(vendedor);
    }

    /**
     * Mapeador interno para transformar la entidad en un DTO estructurado de salida.
     */
    private VendedorResponseDTO mapToResponseDTO(Vendedor vendedor) {
        return VendedorResponseDTO.builder()
                .id(vendedor.getId())
                .codigoInterno(vendedor.getCodigoInterno())
                .rut(vendedor.getRut())
                .nombre(vendedor.getNombre())
                .sucursal(vendedor.getSucursal())
                .porcentajeComision(vendedor.getPorcentajeComision())
                .comisionAcumulada(vendedor.getComisionAcumulada())
                .fechaIngreso(vendedor.getFechaIngreso())
                .build();
    }
}