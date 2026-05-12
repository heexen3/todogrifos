package com.todogrifos.inventarioms.service;

import com.todogrifos.inventarioms.dto.*;
import com.todogrifos.inventarioms.exception.*;
import com.todogrifos.inventarioms.model.Inventario;
import com.todogrifos.inventarioms.repository.InventarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventarioServiceImpl implements InventarioService {

    private final InventarioRepository repository;

    @Override
    @Transactional
    public InventarioResponseDTO crearInventario(InventarioCreateDTO dto) {
        log.info("Intentando registrar inventario para SKU: {}", dto.getSku());

        // Validación de Duplicados
        if (repository.existsBySku(dto.getSku())) {
            log.error("Conflicto: El SKU {} ya existe en el sistema.", dto.getSku());
            throw new SkuDuplicadoException("El SKU " + dto.getSku() + " ya tiene un registro de inventario.");
        }

        // Validación de Lógica de Negocio
        if (dto.getStockMinimo() != null && dto.getStockMinimo() > dto.getCantidad()) {
            log.warn("Carga inconsistente: Stock mínimo ({}) mayor a cantidad inicial ({}).",
                    dto.getStockMinimo(), dto.getCantidad());
            throw new InvalidStockQuantityException("El stock mínimo no puede ser superior a la cantidad inicial disponible.");
        }

        Inventario inventario = Inventario.builder()
                .sku(dto.getSku())
                .cantidad(dto.getCantidad())
                .stockMinimo(dto.getStockMinimo() != null ? dto.getStockMinimo() : 0)
                .build();

        return mapToResponseDTO(repository.save(inventario));
    }

    @Override
    @Transactional(readOnly = true)
    public InventarioResponseDTO obtenerPorSku(String sku) {
        return repository.findBySku(sku)
                .map(this::mapToResponseDTO)
                .orElseThrow(() -> new InventarioNotFoundException("No se encontró registro para el SKU: " + sku));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventarioResponseDTO> obtenerTodo() {
        return repository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public InventarioResponseDTO agregarStock(String sku, StockUpdateDTO dto) {
        log.info("Aumentando stock para SKU: {}. Cantidad: +{}", sku, dto.getCantidad());

        Inventario inventario = repository.findBySku(sku)
                .orElseThrow(() -> new InventarioNotFoundException("No existe registro para el SKU: " + sku));

        inventario.setCantidad(inventario.getCantidad() + dto.getCantidad());
        return mapToResponseDTO(repository.save(inventario));
    }

    @Override
    @Transactional
    public InventarioResponseDTO retirarStock(String sku, StockUpdateDTO dto) {
        log.info("Reduciendo stock para SKU: {}. Cantidad: -{}", sku, dto.getCantidad());

        Inventario inventario = repository.findBySku(sku)
                .orElseThrow(() -> new InventarioNotFoundException("No existe registro para el SKU: " + sku));

        // Validación de Disponibilidad
        if (inventario.getCantidad() < dto.getCantidad()) {
            log.warn("Fallo en retiro: SKU {} solo tiene {} unidades.", sku, inventario.getCantidad());
            throw new InsufficientStockException("Stock insuficiente. Disponible: " + inventario.getCantidad());
        }

        inventario.setCantidad(inventario.getCantidad() - dto.getCantidad());
        return mapToResponseDTO(repository.save(inventario));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validarDisponibilidad(String sku, Integer cantidad) {
        return repository.findBySku(sku)
                .map(inv -> inv.getCantidad() >= cantidad)
                .orElse(false);
    }

    private InventarioResponseDTO mapToResponseDTO(Inventario inventario) {
        return InventarioResponseDTO.builder()
                .id(inventario.getId())
                .sku(inventario.getSku())
                .cantidad(inventario.getCantidad())
                .stockMinimo(inventario.getStockMinimo())
                .enStock(inventario.getCantidad() > 0)
                .build();
    }
}