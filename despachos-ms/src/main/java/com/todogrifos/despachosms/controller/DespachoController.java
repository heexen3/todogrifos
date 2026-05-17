package com.todogrifos.despachosms.controller;

import com.todogrifos.despachosms.dto.DespachoCreateDTO;
import com.todogrifos.despachosms.dto.DespachoResponseDTO;
import com.todogrifos.despachosms.dto.DespachoStatusUpdateDTO;
import com.todogrifos.despachosms.service.DespachoService;
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
public class DespachoController {

    @Autowired
    private DespachoService despachoService;

    /**
     * Endpoint semántico para dar de alta una nueva orden logística de despacho.
     * POST http://localhost:8087/api/despachos
     */
    @PostMapping
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
    public ResponseEntity<DespachoResponseDTO> obtenerPorVentaId(@PathVariable("ventaId") Long ventaId) {
        log.info("Petición HTTP recibida: GET /api/despachos/venta/{} para buscar por boleta.", ventaId);

        DespachoResponseDTO despacho = despachoService.obtenerPorVentaId(ventaId);

        return ResponseEntity.ok(despacho);
    }
}