package com.todogrifos.proveedoresms.controller;

import com.todogrifos.proveedoresms.dto.ProveedorCreateDTO;
import com.todogrifos.proveedoresms.dto.ProveedorResponseDTO;
import com.todogrifos.proveedoresms.service.ProveedorService;
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
@RequestMapping("/api/proveedores")
@Tag(name = "Proveedores", description = "CRUD de proveedores y gestión de información de fabricantes y entidades comerciales")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    /**
     * Endpoint semántico para dar de alta un nuevo fabricante/proveedor.
     * POST http://localhost:8086/api/proveedores
     */
    @PostMapping
    @Operation(
            summary = "Registrar proveedor",
            description = "Permite crear un nuevo proveedor en el sistema con sus datos fiscales y comerciales."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Proveedor creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o incompletos"),
            @ApiResponse(responseCode = "409", description = "Conflicto: proveedor ya existente (ej: RUT duplicado)"),
            @ApiResponse(responseCode = "500", description = "Error interno al registrar proveedor")
    })
    public ResponseEntity<ProveedorResponseDTO> registrarProveedor(@Valid @RequestBody ProveedorCreateDTO dto) {
        log.info("Petición HTTP recibida: POST /api/proveedores para registrar razón social: {}", dto.getRazonSocial());

        ProveedorResponseDTO nuevoProveedor = proveedorService.crearProveedor(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProveedor);
    }

    /**
     * Endpoint semántico para recuperar todos los proveedores registrados.
     * GET http://localhost:8086/api/proveedores
     */
    @GetMapping
    @Operation(
            summary = "Listar proveedores",
            description = "Retorna el catálogo completo de proveedores registrados en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de proveedores obtenido correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno al recuperar proveedores")
    })
    public ResponseEntity<List<ProveedorResponseDTO>> obtenerTodos() {
        log.info("Petición HTTP recibida: GET /api/proveedores para listar catálogo maestro de proveedores.");

        List<ProveedorResponseDTO> lista = proveedorService.obtenerTodos();

        return ResponseEntity.ok(lista);
    }

    /**
     * Endpoint semántico para buscar un proveedor por su ID autoincremental.
     * GET http://localhost:8086/api/proveedores/{id}
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener proveedor por ID",
            description = "Permite consultar la información de un proveedor específico mediante su identificador único."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proveedor encontrado correctamente"),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno al consultar proveedor")
    })
    public ResponseEntity<ProveedorResponseDTO> obtenerPorId(@PathVariable("id") Long id) {
        log.info("Petición HTTP recibida: GET /api/proveedores/{} para búsqueda por ID.", id);

        ProveedorResponseDTO proveedor = proveedorService.obtenerPorId(id);

        return ResponseEntity.ok(proveedor);
    }

    /**
     * Endpoint semántico para buscar un proveedor por su documento fiscal (RUT).
     * GET http://localhost:8086/api/proveedores/rut/{rut}
     */
    @GetMapping("/rut/{rut}")
    @Operation(
            summary = "Obtener proveedor por RUT",
            description = "Permite consultar un proveedor utilizando su RUT o identificador fiscal único."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proveedor encontrado correctamente"),
            @ApiResponse(responseCode = "404", description = "No existe un proveedor con el RUT indicado"),
            @ApiResponse(responseCode = "500", description = "Error interno al consultar proveedor")
    })
    public ResponseEntity<ProveedorResponseDTO> obtenerPorRut(@PathVariable("rut") String rut) {
        log.info("Petición HTTP recibida: GET /api/proveedores/rut/{} para búsqueda por RUT fiscal.", rut);

        ProveedorResponseDTO proveedor = proveedorService.obtenerPorRut(rut);

        return ResponseEntity.ok(proveedor);
    }
}