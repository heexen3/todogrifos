package com.todogrifos.vendedoresms.controller;

import com.todogrifos.vendedoresms.dto.VendedorCreateDTO;
import com.todogrifos.vendedoresms.dto.VendedorResponseDTO;
import com.todogrifos.vendedoresms.dto.ComisionUpdateDTO;
import com.todogrifos.vendedoresms.service.VendedorService;
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
public class VendedorController {

    @Autowired
    private VendedorService vendedorService;

    /**
     * Endpoint semántico para dar de alta a un nuevo ejecutivo de venta.
     * POST http://localhost:8088/api/vendedores
     */
    @PostMapping
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
    public ResponseEntity<VendedorResponseDTO> obtenerPorCodigo(@PathVariable("codigo") String codigo) {
        log.info("Petición HTTP recibida: GET /api/vendedores/codigo/{} para búsqueda institucional.", codigo);

        VendedorResponseDTO vendedor = vendedorService.obtenerPorCodigo(codigo);

        return ResponseEntity.ok(vendedor);
    }
}