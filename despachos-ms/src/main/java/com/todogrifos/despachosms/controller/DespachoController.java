package com.todogrifos.despachosms.controller;

import com.todogrifos.despachosms.dto.DespachoCreateDTO;
import com.todogrifos.despachosms.dto.DespachoResponseDTO;
import com.todogrifos.despachosms.dto.DespachoStatusUpdateDTO;
import com.todogrifos.despachosms.service.DespachoService;
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
@RequestMapping("/api/despachos")
@Tag(name = "Despachos", description = "Gestión de órdenes de despacho y seguimiento de envíos (tracking logístico)")
public class DespachoController {

    @Autowired
    private DespachoService despachoService;

    /**
     * Endpoint semántico para dar de alta una nueva orden logística de despacho.
     * POST http://localhost:8087/api/despachos
     */
    @PostMapping
    @Operation(
            summary = "Crear una nueva orden de despacho",
            description = "Registra una orden de despacho asociada a una venta y genera un código de seguimiento para tracking logístico."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Despacho creado exitosamente con código de seguimiento"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o error en la creación del despacho"),
            @ApiResponse(responseCode = "409", description = "Despacho duplicado o conflicto de negocio"),
            @ApiResponse(responseCode = "500", description = "Error interno del sistema o falla en dependencias")
    })
    public ResponseEntity<DespachoResponseDTO> crearDespacho(@Valid @RequestBody DespachoCreateDTO dto) {
        log.info("Petición HTTP recibida: POST /api/despachos para registrar Tracking: {}", dto.getCodigoSeguimiento());

        DespachoResponseDTO nuevoDespacho = despachoService.crearDespacho(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoDespacho);
    }

    /**
     * Endpoint semántico para actualizar el estado en la máquina de tránsito (PENDIENTE -> EN_RUTA -> ENTREGADO).
     * PUT http://localhost:8087/api/despachos/{id}/estado
     */
    @PutMapping("/{id}/estado")
    @Operation(
            summary = "Actualizar estado de un despacho",
            description = "Permite actualizar el estado de una orden de despacho (PENDIENTE, EN_RUTA, ENTREGADO) durante su ciclo logístico."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado del despacho actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o transición de estado no permitida"),
            @ApiResponse(responseCode = "404", description = "Despacho no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del sistema o falla en el procesamiento")
    })
    public ResponseEntity<DespachoResponseDTO> actualizarEstado(
            @PathVariable("id") Long id,
            @Valid @RequestBody DespachoStatusUpdateDTO dto) {
        log.info("Petición HTTP recibida: PUT /api/despachos/{}/estado con payload: {}", id, dto.getEstado());

        DespachoResponseDTO despachoActualizado = despachoService.actualizarEstado(id, dto);

        return ResponseEntity.ok(despachoActualizado);
    }

    /**
     * Endpoint semántico para listar la bitácora completa de despachos.
     * GET http://localhost:8087/api/despachos
     */
    @GetMapping
    @Operation(
            summary = "Listar todos los despachos",
            description = "Retorna el historial completo de órdenes de despacho registradas en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de despachos obtenido exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno al recuperar el historial de despachos")
    })
    public ResponseEntity<List<DespachoResponseDTO>> obtenerTodos() {
        log.info("Petición HTTP recibida: GET /api/despachos para listar histórico.");

        List<DespachoResponseDTO> lista = despachoService.obtenerTodos();

        return ResponseEntity.ok(lista);
    }

    /**
     * Endpoint semántico para rastrear un despacho mediante su código único de Tracking.
     * GET http://localhost:8087/api/despachos/tracking/{codigo}
     */
    @GetMapping("/tracking/{codigo}")
    @Operation(
            summary = "Rastrear despacho por código de seguimiento",
            description = "Permite obtener la información de un despacho utilizando su código único de tracking logístico."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Despacho encontrado correctamente"),
            @ApiResponse(responseCode = "404", description = "No existe un despacho con el código de seguimiento proporcionado"),
            @ApiResponse(responseCode = "500", description = "Error interno al procesar la consulta de tracking")
    })
    public ResponseEntity<DespachoResponseDTO> obtenerPorTracking(@PathVariable("codigo") String codigo) {
        log.info("Petición HTTP recibida: GET /api/despachos/tracking/{} para rastreo.", codigo);

        DespachoResponseDTO despacho = despachoService.obtenerPorTracking(codigo);

        return ResponseEntity.ok(despacho);
    }

    /**
     * Endpoint semántico para consultar el despacho de una boleta específica.
     * GET http://localhost:8087/api/despachos/venta/{ventaId}
     */
    @GetMapping("/venta/{ventaId}")
    @Operation(
            summary = "Obtener despacho por ID de venta",
            description = "Permite consultar el despacho asociado a una venta específica utilizando su identificador."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Despacho encontrado correctamente"),
            @ApiResponse(responseCode = "404", description = "No existe despacho asociado a la venta indicada"),
            @ApiResponse(responseCode = "500", description = "Error interno al procesar la consulta")
    })
    public ResponseEntity<DespachoResponseDTO> obtenerPorVentaId(@PathVariable("ventaId") Long ventaId) {
        log.info("Petición HTTP recibida: GET /api/despachos/venta/{} para buscar por boleta.", ventaId);

        DespachoResponseDTO despacho = despachoService.obtenerPorVentaId(ventaId);

        return ResponseEntity.ok(despacho);
    }
}