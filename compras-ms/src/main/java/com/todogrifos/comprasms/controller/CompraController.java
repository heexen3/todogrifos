package com.todogrifos.comprasms.controller;

import com.todogrifos.comprasms.dto.CompraCreateDTO;
import com.todogrifos.comprasms.dto.CompraResponseDTO;
import com.todogrifos.comprasms.service.CompraService;
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
@RequestMapping("/api/compras")
@Tag(name = "Compras", description = "CRegistro de compras y aumento de stock")
public class CompraController {

    @Autowired
    private CompraService compraService;

    /**
     * Endpoint semántico para registrar una nueva factura de abastecimiento e inyectar stock.
     * POST http://localhost:8085/api/compras
     */
    @PostMapping
    @Operation(
            summary = "Registrar una nueva compra",
            description = "Registra una compra a proveedor, valida la factura, procesa la operación y actualiza el stock en inventario-ms."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Compra registrada exitosamente y stock actualizado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o compra rechazada por reglas de negocio"),
            @ApiResponse(responseCode = "409", description = "Factura de proveedor ya registrada (duplicada)"),
            @ApiResponse(responseCode = "500", description = "Error interno del sistema o falla en dependencias")
    })
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
    @Operation(
            summary = "Listar todas las compras",
            description = "Retorna el historial completo de compras registradas en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de compras obtenido exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno al recuperar el historial de compras")
    })
    public ResponseEntity<List<CompraResponseDTO>> listarTodas() {
        log.info("Petición HTTP recibida: GET /api/compras para listar historial de compras.");

        List<CompraResponseDTO> compras = compraService.obtenerTodas();

        return ResponseEntity.ok(compras);
    }
}