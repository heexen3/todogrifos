package com.todogrifos.inventarioms.controller;

import com.todogrifos.inventarioms.dto.*;
import com.todogrifos.inventarioms.service.InventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/inventario")
@RequiredArgsConstructor
@Tag(name = "Inventario", description = "Control de stock por SKU, validación de disponibilidad y gestión de movimientos de inventario")
public class InventarioController {

    private final InventarioService service;

    @PostMapping
    @Operation(
            summary = "Crear registro de inventario",
            description = "Permite crear un registro inicial de stock asociado a un SKU dentro del sistema de inventario."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Inventario creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o SKU no válido"),
            @ApiResponse(responseCode = "409", description = "El SKU ya existe en el inventario"),
            @ApiResponse(responseCode = "500", description = "Error interno al crear el registro de inventario")
    })
    public ResponseEntity<InventarioResponseDTO> crear(@Valid @RequestBody InventarioCreateDTO dto) {
        log.info("REST request para crear inventario: {}", dto.getSku());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crearInventario(dto));
    }

    @GetMapping("/{sku}")
    @Operation(
            summary = "Obtener inventario por SKU",
            description = "Permite consultar el stock disponible de un producto específico utilizando su código SKU."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventario encontrado correctamente"),
            @ApiResponse(responseCode = "404", description = "No existe inventario asociado al SKU"),
            @ApiResponse(responseCode = "500", description = "Error interno al consultar el inventario")
    })
    public ResponseEntity<InventarioResponseDTO> obtenerPorSku(@PathVariable String sku) {
        log.info("REST request para obtener stock del SKU: {}", sku);
        return ResponseEntity.ok(service.obtenerPorSku(sku));
    }

    @GetMapping
    @Operation(
            summary = "Listar todo el inventario",
            description = "Retorna el listado completo de productos registrados en el sistema de inventario con su stock actual."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de inventario obtenido exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno al recuperar el inventario")
    })
    public ResponseEntity<List<InventarioResponseDTO>> obtenerTodos() {
        log.info("REST request para listar todo el inventario");
        return ResponseEntity.ok(service.obtenerTodo());
    }

    // Usamos PATCH para actualizaciones parciales (sumar stock)
    @PutMapping("/{sku}/agregar")
    @Operation(
            summary = "Agregar stock a un producto",
            description = "Permite incrementar la cantidad de stock disponible para un SKU específico en el inventario."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o cantidad no válida"),
            @ApiResponse(responseCode = "404", description = "SKU no encontrado en el inventario"),
            @ApiResponse(responseCode = "500", description = "Error interno al actualizar el stock")
    })
    public ResponseEntity<InventarioResponseDTO> agregarStock(
            @PathVariable String sku,
            @Valid @RequestBody StockUpdateDTO dto) {
        log.info("REST request para agregar stock a SKU: {}", sku);
        return ResponseEntity.ok(service.agregarStock(sku, dto));
    }

    @PutMapping("/{sku}/retirar")
    @Operation(
            summary = "Retirar stock de un producto",
            description = "Permite disminuir la cantidad de stock disponible para un SKU específico en el inventario."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock retirado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o cantidad no válida"),
            @ApiResponse(responseCode = "404", description = "SKU no encontrado en el inventario"),
            @ApiResponse(responseCode = "500", description = "Error interno al actualizar el stock")
    })
    public ResponseEntity<InventarioResponseDTO> retirarStock(
            @PathVariable String sku,
            @Valid @RequestBody StockUpdateDTO dto) {
        log.info("REST request para retirar stock de SKU: {}", sku);
        return ResponseEntity.ok(service.retirarStock(sku, dto));
    }

    // Endpoint clave para la comunicación entre microservicios (Feign)
    @GetMapping("/validar/{sku}")
    @Operation(
            summary = "Validar disponibilidad de stock",
            description = "Permite verificar si existe stock suficiente para un SKU específico, utilizado por otros microservicios mediante OpenFeign."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Validación realizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos (SKU o cantidad)"),
            @ApiResponse(responseCode = "500", description = "Error interno al validar el stock")
    })
    public ResponseEntity<Boolean> validarStock(
            @PathVariable String sku,
            @RequestParam Integer cantidad) {
        log.info("REST request para validar disponibilidad: SKU {} - Cantidad {}", sku, cantidad);
        return ResponseEntity.ok(service.validarDisponibilidad(sku, cantidad));
    }
}