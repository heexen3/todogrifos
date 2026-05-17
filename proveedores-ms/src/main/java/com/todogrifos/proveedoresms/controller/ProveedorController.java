package com.todogrifos.proveedoresms.controller;

import com.todogrifos.proveedoresms.dto.ProveedorCreateDTO;
import com.todogrifos.proveedoresms.dto.ProveedorResponseDTO;
import com.todogrifos.proveedoresms.service.ProveedorService;
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
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    /**
     * Endpoint semántico para dar de alta un nuevo fabricante/proveedor.
     * POST http://localhost:8086/api/proveedores
     */
    @PostMapping
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
    public ResponseEntity<ProveedorResponseDTO> obtenerPorRut(@PathVariable("rut") String rut) {
        log.info("Petición HTTP recibida: GET /api/proveedores/rut/{} para búsqueda por RUT fiscal.", rut);

        ProveedorResponseDTO proveedor = proveedorService.obtenerPorRut(rut);

        return ResponseEntity.ok(proveedor);
    }
}