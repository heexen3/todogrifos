package com.todogrifos.ventasms.controller;

import com.todogrifos.ventasms.dto.VentaCreateDTO;
import com.todogrifos.ventasms.dto.VentaResponseDTO;
import com.todogrifos.ventasms.service.VentaService;
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
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @PostMapping
    public ResponseEntity<VentaResponseDTO> procesarVenta(@Valid @RequestBody VentaCreateDTO dto) {
        log.info("Petición HTTP recibida: POST /api/ventas para emitir boleta con Folio: {}", dto.getFolio());

        VentaResponseDTO nuevaVenta = ventaService.procesarVenta(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaVenta);
    }

    @GetMapping
    public ResponseEntity<List<VentaResponseDTO>> listarTodas() {
        log.info("Petición HTTP recibida: GET /api/ventas para listar histórico de facturación.");

        List<VentaResponseDTO> ventas = ventaService.obtenerTodas();

        return ResponseEntity.ok(ventas);
    }
}