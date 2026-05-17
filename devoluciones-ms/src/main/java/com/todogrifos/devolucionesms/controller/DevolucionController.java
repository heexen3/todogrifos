package com.todogrifos.devolucionesms.controller;

import com.todogrifos.devolucionesms.dto.DevolucionCreateDTO;
import com.todogrifos.devolucionesms.dto.DevolucionResponseDTO;
import com.todogrifos.devolucionesms.service.DevolucionService;
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
public class DevolucionController {

    @Autowired
    private DevolucionService devolucionService;

    /**
     * Endpoint semántico para procesar y dar de alta una Nota de Crédito / Devolución técnica.
     * POST http://localhost:8089/api/devoluciones
     */
    @PostMapping
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
    public ResponseEntity<List<DevolucionResponseDTO>> obtenerPorVentaId(@PathVariable("ventaId") Long ventaId) {
        log.info("Petición HTTP recibida: GET /api/devoluciones/venta/{} para auditoría.", ventaId);

        List<DevolucionResponseDTO> lista = devolucionService.obtenerPorVentaId(ventaId);

        return ResponseEntity.ok(lista);
    }
}