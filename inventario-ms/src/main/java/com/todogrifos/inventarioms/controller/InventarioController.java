package com.todogrifos.inventarioms.controller;

import com.todogrifos.inventarioms.dto.*;
import com.todogrifos.inventarioms.service.InventarioService;
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
public class InventarioController {

    private final InventarioService service;

    @PostMapping
    public ResponseEntity<InventarioResponseDTO> crear(@Valid @RequestBody InventarioCreateDTO dto) {
        log.info("REST request para crear inventario: {}", dto.getSku());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crearInventario(dto));
    }

    @GetMapping("/{sku}")
    public ResponseEntity<InventarioResponseDTO> obtenerPorSku(@PathVariable String sku) {
        log.info("REST request para obtener stock del SKU: {}", sku);
        return ResponseEntity.ok(service.obtenerPorSku(sku));
    }

    @GetMapping
    public ResponseEntity<List<InventarioResponseDTO>> obtenerTodos() {
        log.info("REST request para listar todo el inventario");
        return ResponseEntity.ok(service.obtenerTodo());
    }

    // Usamos PATCH para actualizaciones parciales (sumar stock)
    @PutMapping("/{sku}/agregar")
    public ResponseEntity<InventarioResponseDTO> agregarStock(
            @PathVariable String sku,
            @Valid @RequestBody StockUpdateDTO dto) {
        log.info("REST request para agregar stock a SKU: {}", sku);
        return ResponseEntity.ok(service.agregarStock(sku, dto));
    }

    @PutMapping("/{sku}/retirar")
    public ResponseEntity<InventarioResponseDTO> retirarStock(
            @PathVariable String sku,
            @Valid @RequestBody StockUpdateDTO dto) {
        log.info("REST request para retirar stock de SKU: {}", sku);
        return ResponseEntity.ok(service.retirarStock(sku, dto));
    }

    // Endpoint clave para la comunicación entre microservicios (Feign)
    @GetMapping("/validar/{sku}")
    public ResponseEntity<Boolean> validarStock(
            @PathVariable String sku,
            @RequestParam Integer cantidad) {
        log.info("REST request para validar disponibilidad: SKU {} - Cantidad {}", sku, cantidad);
        return ResponseEntity.ok(service.validarDisponibilidad(sku, cantidad));
    }
}