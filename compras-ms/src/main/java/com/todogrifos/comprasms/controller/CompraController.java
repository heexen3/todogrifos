package com.todogrifos.comprasms.controller;

import com.todogrifos.comprasms.dto.CompraCreateDTO;
import com.todogrifos.comprasms.dto.CompraResponseDTO;
import com.todogrifos.comprasms.service.CompraService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/compras")
public class CompraController {

    @Autowired
    private CompraService compraService;

    /**
     * Endpoint semántico para registrar una nueva factura de abastecimiento e inyectar stock.
     * POST http://localhost:8085/api/compras
     */
    @PostMapping
    public ResponseEntity<CompraResponseDTO> registrarCompra(@Valid @RequestBody CompraCreateDTO dto) {
        log.info("Petición HTTP recibida: POST /api/compras para procesar Factura N°: {}", dto.getNumeroFactura());

        CompraResponseDTO nuevaCompra = compraService.procesarCompra(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCompra);
    }

    /**
     * Endpoint semántico para recuperar el historial completo de compras a proveedores.
     * GET http://localhost:8085/api/compras
     */
    @GetMapping
    public ResponseEntity<List<CompraResponseDTO>> listarTodas() {
        log.info("Petición HTTP recibida: GET /api/compras para listar historial de compras.");

        List<CompraResponseDTO> compras = compraService.obtenerTodas();

        return ResponseEntity.ok(compras);
    }
}