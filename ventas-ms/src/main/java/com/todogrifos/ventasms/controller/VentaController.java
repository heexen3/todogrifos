package com.todogrifos.ventasms.controller;

import com.todogrifos.ventasms.dto.VentaCreateDTO;
import com.todogrifos.ventasms.dto.VentaResponseDTO;
import com.todogrifos.ventasms.service.VentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/ventas")
@Tag(name = "Ventas", description = "Registro de ventas, emisión de boletas y descuento automático de stock en inventario")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @PostMapping
    @Operation(
            summary = "Procesar venta",
            description = "Registra una nueva venta, genera la boleta y ejecuta el descuento de stock en inventario."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Venta procesada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o stock insuficiente"),
            @ApiResponse(responseCode = "409", description = "Conflicto de negocio (ej: folio duplicado)"),
            @ApiResponse(responseCode = "500", description = "Error interno al procesar la venta")
    })
    public ResponseEntity<VentaResponseDTO> procesarVenta(@Valid @RequestBody VentaCreateDTO dto) {
        log.info("Petición HTTP recibida: POST /api/ventas para emitir boleta con Folio: {}", dto.getFolio());

        VentaResponseDTO nuevaVenta = ventaService.procesarVenta(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaVenta);
    }

    @GetMapping
    @Operation(
            summary = "Listar ventas",
            description = "Retorna el historial completo de ventas registradas en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de ventas obtenido correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno al recuperar ventas")
    })
    public ResponseEntity<List<VentaResponseDTO>> listarTodas() {
        log.info("Petición HTTP recibida: GET /api/ventas para listar histórico de facturación.");

        List<VentaResponseDTO> ventas = ventaService.obtenerTodas();

        return ResponseEntity.ok(ventas);
    }
}