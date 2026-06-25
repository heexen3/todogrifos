package com.todogrifos.vendedoresms.controller;

import com.todogrifos.vendedoresms.dto.VendedorCreateDTO;
import com.todogrifos.vendedoresms.dto.VendedorResponseDTO;
import com.todogrifos.vendedoresms.dto.ComisionUpdateDTO;
import com.todogrifos.vendedoresms.service.VendedorService;
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
@RequestMapping("/api/vendedores")
@Tag(name = "Vendedores", description = "CRUD de vendedores, gestión de nómina y acumulación de comisiones por ventas")
public class VendedorController {

    @Autowired
    private VendedorService vendedorService;

    /**
     * Endpoint semántico para dar de alta a un nuevo ejecutivo de venta.
     * POST http://localhost:8088/api/vendedores
     */
    @PostMapping
    @Operation(
            summary = "Crear vendedor",
            description = "Permite registrar un nuevo vendedor en el sistema con sus datos personales y comerciales."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vendedor creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o incompletos"),
            @ApiResponse(responseCode = "409", description = "Conflicto: vendedor ya existente"),
            @ApiResponse(responseCode = "500", description = "Error interno al crear vendedor")
    })
    public ResponseEntity<VendedorResponseDTO> crearVendedor(@Valid @RequestBody VendedorCreateDTO dto) {
        log.info("Petición HTTP recibida: POST /api/vendedores para dar de alta a: {}", dto.getNombre());

        VendedorResponseDTO nuevoVendedor = vendedorService.crearVendedor(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoVendedor);
    }

    /**
     * Endpoint distribuido semántico para acumular comisiones de dinero real tras una venta.
     * PUT http://localhost:8088/api/vendedores/{id}/comision
     */
    @PutMapping("/{id}/comision")
    @Operation(
            summary = "Actualizar comisión del vendedor",
            description = "Permite acumular o actualizar la comisión de un vendedor en base a una venta realizada."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comisión actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o monto de comisión no válido"),
            @ApiResponse(responseCode = "404", description = "Vendedor no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno al actualizar comisión")
    })
    public ResponseEntity<VendedorResponseDTO> acumularComision(
            @PathVariable("id") Long id,
            @Valid @RequestBody ComisionUpdateDTO dto) {
        log.info("Petición HTTP distribuida: PUT /api/vendedores/{}/comision. Procesando impacto de venta.", id);

        VendedorResponseDTO vendedorActualizado = vendedorService.acumularComision(id, dto);

        return ResponseEntity.ok(vendedorActualizado);
    }

    /**
     * Endpoint semántico para recuperar toda la nómina de vendedores activos.
     * GET http://localhost:8088/api/vendedores
     */
    @GetMapping
    @Operation(
            summary = "Listar vendedores",
            description = "Retorna la nómina completa de vendedores registrados en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de vendedores obtenido correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno al recuperar vendedores")
    })
    public ResponseEntity<List<VendedorResponseDTO>> obtenerTodos() {
        log.info("Petición HTTP recibida: GET /api/vendedores para listar nómina de personal.");

        List<VendedorResponseDTO> lista = vendedorService.obtenerTodos();

        return ResponseEntity.ok(lista);
    }

    /**
     * Endpoint semántico para buscar un vendedor por su ID incremental.
     * GET http://localhost:8088/api/vendedores/{id}
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener vendedor por ID",
            description = "Permite consultar la información de un vendedor utilizando su identificador único."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vendedor encontrado correctamente"),
            @ApiResponse(responseCode = "404", description = "Vendedor no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno al consultar vendedor")
    })
    public ResponseEntity<VendedorResponseDTO> obtenerPorId(@PathVariable("id") Long id) {
        log.info("Petición HTTP recibida: GET /api/vendedores/{} para búsqueda por ID.", id);

        VendedorResponseDTO vendedor = vendedorService.obtenerPorId(id);

        return ResponseEntity.ok(vendedor);
    }

    /**
     * Endpoint semántico para buscar un vendedor por su código interno de empleado.
     * GET http://localhost:8088/api/vendedores/codigo/{codigo}
     */
    @GetMapping("/codigo/{codigo}")
    @Operation(
            summary = "Obtener vendedor por código",
            description = "Permite consultar un vendedor utilizando su código interno institucional."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vendedor encontrado correctamente"),
            @ApiResponse(responseCode = "404", description = "No existe un vendedor con el código indicado"),
            @ApiResponse(responseCode = "500", description = "Error interno al consultar vendedor")
    })
    public ResponseEntity<VendedorResponseDTO> obtenerPorCodigo(@PathVariable("codigo") String codigo) {
        log.info("Petición HTTP recibida: GET /api/vendedores/codigo/{} para búsqueda institucional.", codigo);

        VendedorResponseDTO vendedor = vendedorService.obtenerPorCodigo(codigo);

        return ResponseEntity.ok(vendedor);
    }
}