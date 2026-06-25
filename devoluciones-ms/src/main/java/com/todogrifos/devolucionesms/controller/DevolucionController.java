package com.todogrifos.devolucionesms.controller;

import com.todogrifos.devolucionesms.dto.DevolucionCreateDTO;
import com.todogrifos.devolucionesms.dto.DevolucionResponseDTO;
import com.todogrifos.devolucionesms.service.DevolucionService;
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
@RequestMapping("/api/devoluciones")
@Tag(name = "Devoluciones", description = "Gestión de notas de crédito, devoluciones y reingreso o merma de inventario")
public class DevolucionController {

    @Autowired
    private DevolucionService devolucionService;

    /**
     * Endpoint semántico para procesar y dar de alta una Nota de Crédito / Devolución técnica.
     * POST http://localhost:8089/api/devoluciones
     */
    @PostMapping
    @Operation(
            summary = "Registrar una nueva devolución o nota de crédito",
            description = "Procesa una devolución asociada a una venta, generando una nota de crédito y realizando el reingreso o ajuste de inventario según corresponda."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Devolución registrada exitosamente y stock actualizado si corresponde"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o error en la validación de la devolución"),
            @ApiResponse(responseCode = "409", description = "Conflicto de negocio (ej: nota de crédito duplicada)"),
            @ApiResponse(responseCode = "500", description = "Error interno del sistema o fallo en el procesamiento de la devolución")
    })
    public ResponseEntity<DevolucionResponseDTO> procesarDevolucion(@Valid @RequestBody DevolucionCreateDTO dto) {
        log.info("Petición HTTP recibida: POST /api/devoluciones para Folio: {}", dto.getNotaCreditoFolio());

        DevolucionResponseDTO nuevaDevolucion = devolucionService.procesarDevolucion(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaDevolucion);
    }

    /**
     * Endpoint semántico para recuperar el histórico completo de Notas de Crédito del retail.
     * GET http://localhost:8089/api/devoluciones
     */
    @GetMapping
    @Operation(
            summary = "Listar todas las devoluciones",
            description = "Retorna el historial completo de notas de crédito y devoluciones registradas en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de devoluciones obtenido exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno al recuperar el historial de devoluciones")
    })
    public ResponseEntity<List<DevolucionResponseDTO>> obtenerTodas() {
        log.info("Petición HTTP recibida: GET /api/devoluciones para listar histórico de post-venta.");

        List<DevolucionResponseDTO> lista = devolucionService.obtenerTodas();

        return ResponseEntity.ok(lista);
    }

    /**
     * Endpoint semántico para listar las Notas de Crédito asociadas a una boleta específica.
     * GET http://localhost:8089/api/devoluciones/venta/{ventaId}
     */
    @GetMapping("/venta/{ventaId}")
    @Operation(
            summary = "Listar devoluciones por venta",
            description = "Permite obtener todas las notas de crédito y devoluciones asociadas a una venta específica."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Devoluciones encontradas correctamente"),
            @ApiResponse(responseCode = "404", description = "No existen devoluciones asociadas a la venta indicada"),
            @ApiResponse(responseCode = "500", description = "Error interno al consultar las devoluciones")
    })
    public ResponseEntity<List<DevolucionResponseDTO>> obtenerPorVentaId(@PathVariable("ventaId") Long ventaId) {
        log.info("Petición HTTP recibida: GET /api/devoluciones/venta/{} para auditoría.", ventaId);

        List<DevolucionResponseDTO> lista = devolucionService.obtenerPorVentaId(ventaId);

        return ResponseEntity.ok(lista);
    }
}