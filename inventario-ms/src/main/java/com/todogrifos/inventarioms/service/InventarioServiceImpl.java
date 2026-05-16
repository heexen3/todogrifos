package com.todogrifos.inventarioms.service;

import com.todogrifos.inventarioms.client.ProductoClient;
import com.todogrifos.inventarioms.dto.InventarioCreateDTO;
import com.todogrifos.inventarioms.dto.InventarioResponseDTO;
import com.todogrifos.inventarioms.dto.StockUpdateDTO;
import com.todogrifos.inventarioms.dto.ProductoDTO;
import com.todogrifos.inventarioms.exception.InsufficientStockException;
import com.todogrifos.inventarioms.exception.InvalidStockQuantityException;
import com.todogrifos.inventarioms.exception.InventarioNotFoundException;
import com.todogrifos.inventarioms.exception.SkuDuplicadoException;
import com.todogrifos.inventarioms.model.Inventario;
import com.todogrifos.inventarioms.repository.InventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventarioServiceImpl implements InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;

    // Inyectamos el cliente Feign para conectar con productos-ms
    @Autowired
    private ProductoClient productoClient;

    @Override
    public InventarioResponseDTO crearInventario(InventarioCreateDTO dto) {
        // 1. VALIDACIÓN REMOTA:
        // Delegamos la petición directamente a Feign. Si el SKU no existe o el servicio
        // remoto falla, la excepción subirá de forma natural al GlobalExceptionHandler.
        ProductoDTO productoRemoto = productoClient.getProductoBySku(dto.getSku());

        if (productoRemoto == null) {
            throw new InventarioNotFoundException("El producto con SKU " + dto.getSku() + " no existe en el catálogo maestro.");
        }

        // 2. Validaciones locales en la base de datos de inventario
        if (inventarioRepository.existsBySku(dto.getSku())) {
            throw new SkuDuplicadoException("Ya existe un registro de inventario para el SKU: " + dto.getSku());
        }

        if (dto.getCantidad() < 0) {
            throw new InvalidStockQuantityException("La cantidad inicial no puede ser negativa");
        }

        // 3. Construcción y persistencia del modelo
        Inventario inventario = Inventario.builder()
                .sku(dto.getSku())
                .cantidad(dto.getCantidad())
                .build();

        Inventario guardado = inventarioRepository.save(inventario);
        return mapToResponseDTO(guardado);
    }

    @Override
    public InventarioResponseDTO obtenerPorSku(String sku) {
        Inventario inventario = inventarioRepository.findBySku(sku)
                .orElseThrow(() -> new InventarioNotFoundException("Inventario no encontrado para el SKU: " + sku));
        return mapToResponseDTO(inventario);
    }

    @Override
    public List<InventarioResponseDTO> obtenerTodo() { // <- Corregido nombre según interfaz
        return inventarioRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public InventarioResponseDTO agregarStock(String sku, StockUpdateDTO dto) { // <- Implementado según interfaz
        Inventario inventario = inventarioRepository.findBySku(sku)
                .orElseThrow(() -> new InventarioNotFoundException("Inventario no encontrado para el SKU: " + sku));

        if (dto.getCantidadModificada() <= 0) {
            throw new InvalidStockQuantityException("La cantidad a agregar debe ser mayor a cero.");
        }

        int nuevaCantidad = inventario.getCantidad() + dto.getCantidadModificada();
        inventario.setCantidad(nuevaCantidad);

        Inventario actualizado = inventarioRepository.save(inventario);
        return mapToResponseDTO(actualizado);
    }

    @Override
    public InventarioResponseDTO retirarStock(String sku, StockUpdateDTO dto) { // <- Implementado según interfaz
        Inventario inventario = inventarioRepository.findBySku(sku)
                .orElseThrow(() -> new InventarioNotFoundException("Inventario no encontrado para el SKU: " + sku));

        if (dto.getCantidadModificada() <= 0) {
            throw new InvalidStockQuantityException("La cantidad a retirar debe ser mayor a cero.");
        }

        int nuevaCantidad = inventario.getCantidad() - dto.getCantidadModificada();

        if (nuevaCantidad < 0) {
            throw new InsufficientStockException("Stock insuficiente para realizar el retiro. Stock actual: " + inventario.getCantidad());
        }

        inventario.setCantidad(nuevaCantidad);

        Inventario actualizado = inventarioRepository.save(inventario);
        return mapToResponseDTO(actualizado);
    }

    @Override
    public boolean validarDisponibilidad(String sku, Integer cantidad) { // <- Implementado según interfaz
        Inventario inventario = inventarioRepository.findBySku(sku)
                .orElseThrow(() -> new InventarioNotFoundException("No se encontró registro de inventario para el SKU: " + sku));

        if (cantidad <= 0) {
            throw new InvalidStockQuantityException("La cantidad solicitada para validar debe ser mayor a cero.");
        }

        if (inventario.getCantidad() < cantidad) {
            throw new InsufficientStockException("Stock insuficiente para el SKU: " + sku + ". Disponible: " + inventario.getCantidad());
        }

        return true;
    }

    private InventarioResponseDTO mapToResponseDTO(Inventario inventario) {
        return InventarioResponseDTO.builder()
                .id(inventario.getId())
                .sku(inventario.getSku())
                .cantidad(inventario.getCantidad())
                .stockMinimo(inventario.getStockMinimo() != null ? inventario.getStockMinimo() : 0)
                .enStock(inventario.getCantidad() > 0)
                .build();
    }
}